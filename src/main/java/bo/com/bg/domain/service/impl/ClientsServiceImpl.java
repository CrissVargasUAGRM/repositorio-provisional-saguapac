package bo.com.bg.domain.service.impl;

import bo.com.bg.app.rest.request.ClientRequest;
import bo.com.bg.app.rest.response.ClientClienteGroup;
import bo.com.bg.app.rest.response.ClientResponseClient;
import bo.com.bg.app.rest.response.ClientResponseData;
import bo.com.bg.commons.enums.ErrorMessage;
import bo.com.bg.commons.exceptions.ProviderException;
import bo.com.bg.domain.config.ProviderSaguapacConfig;
import bo.com.bg.domain.connector.InvoicePendingConnector;
import bo.com.bg.domain.connector.response.InvoicePendingResponseDTO;
import bo.com.bg.domain.mocks.InvoicePendingResponseMock;
import bo.com.bg.domain.service.ClientsService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ClientsServiceImpl implements ClientsService {

    private static final Logger LOG = LoggerFactory.getLogger(ClientsServiceImpl.class);

    private static final String FILTER_CODIGO_SOCIO = "codigoSocio";

    private final InvoicePendingConnector invoicePendingConnector;
    private final ProviderSaguapacConfig config;

    public ClientsServiceImpl(InvoicePendingConnector invoicePendingConnector, ProviderSaguapacConfig config) {
        this.invoicePendingConnector = invoicePendingConnector;
        this.config = config;
    }

    @Override
    public ClientResponseData getClient(ClientRequest payload) {
        if (payload == null) {
            throw new IllegalArgumentException("406 - payload requerido");
        }
        if (payload.getData() == null) {
            throw new IllegalArgumentException("406 - data requerido");
        }
        if (payload.getData().getFiltro() == null || payload.getData().getFiltro().isEmpty()) {
            throw new IllegalArgumentException("406 - filtros requeridos");
        }
        if (payload.getData().getServiceId() == null) {
            throw new IllegalArgumentException("406 - serviceId requerido");
        }

        String codigoSocio = extractCodigoSocio(payload);
        if (codigoSocio == null || codigoSocio.isBlank()) {
            throw new IllegalArgumentException("406 - valor de codigoSocio requerido");
        }
        codigoSocio = sanitize(codigoSocio);

        Integer serviceId = payload.getData().getServiceId();

        InvoicePendingResponseDTO providerResponse;
        try {
            if (config.isMockEnabled()) {
                providerResponse = InvoicePendingResponseMock.sample();
            } else {
                providerResponse = invoicePendingConnector.getPendingInvoices(codigoSocio);
            }
        } catch (ProviderException e) {
            LOG.warn("Error proveedor Saguapac clientes: {}", e.getMessage());
            throw new ProviderException(
                    ErrorMessage.ERROR_SERVICE_EXTERNO.getMsg(),
                    ErrorMessage.ERROR_SERVICE_EXTERNO.getCod(),
                    HttpStatus.NOT_ACCEPTABLE);
        } catch (IllegalStateException e) {
            throw new IllegalArgumentException("406 - " + e.getMessage());
        }

        return buildResponse(providerResponse, serviceId);
    }

    private static String extractCodigoSocio(ClientRequest payload) {
        return payload.getData().getFiltro().stream()
                .filter(Objects::nonNull)
                .filter(f -> FILTER_CODIGO_SOCIO.equalsIgnoreCase(f.getAlias()))
                .map(ClientRequest.FiltroItem::getValor)
                .findFirst()
                .orElse(null);
    }

    private static String sanitize(String raw) {
        return Jsoup.clean(raw, Safelist.basic());
    }

    private static ClientResponseData buildResponse(InvoicePendingResponseDTO dto, Integer serviceId) {
        if (dto == null || dto.getPartner() == null) {
            return ClientResponseData.builder()
                    .serviceId(serviceId)
                    .clientes(Collections.emptyList())
                    .build();
        }

        InvoicePendingResponseDTO.PartnerDto p = dto.getPartner();
        List<ClientResponseClient> fields = new ArrayList<>();
        fields.add(labeledField("idConexion", p.getConnectionId(), false));
        fields.add(labeledField("codigoConexion", p.getConnectionCode(), true));
        fields.add(labeledField("nombre", p.getFirstName(), false));
        fields.add(labeledField("apellidos", p.getLastName(), false));
        fields.add(labeledField("direccion", p.getAddress(), false));
        fields.add(labeledField("ultimaFechaFacturaPagada", p.getLastPaidInvoiceDate(), false));
        fields.add(labeledField("numeroDocumento", p.getDocumentNumber(), false));
        fields.add(labeledField("correoElectronico", p.getEmail(), false));
        fields.add(labeledField("celular", p.getMobile(), false));

        String fullName = joinNameParts(p.getFirstName(), p.getLastName());
        if (!fullName.isBlank()) {
            fields.add(0, ClientResponseClient.builder()
                    .label("cliente")
                    .value(sanitize(fullName))
                    .mandatory(false)
                    .editable("N")
                    .description("cliente")
                    .code("cliente")
                    .build());
        }

        ClientClienteGroup row = ClientClienteGroup.builder().cliente(fields).build();

        return ClientResponseData.builder()
                .serviceId(serviceId)
                .clientes(Collections.singletonList(row))
                .build();
    }

    private static ClientResponseClient labeledField(String code, String value, boolean mandatory) {
        String v = value != null ? sanitize(value) : "";
        return ClientResponseClient.builder()
                .label(code)
                .value(v)
                .mandatory(mandatory)
                .editable("N")
                .description(code)
                .code(code)
                .build();
    }

    private static String joinNameParts(String first, String last) {
        StringBuilder sb = new StringBuilder();
        if (first != null && !first.isBlank()) {
            sb.append(first.trim());
        }
        if (last != null && !last.isBlank()) {
            if (sb.length() > 0) {
                sb.append(' ');
            }
            sb.append(last.trim());
        }
        return sb.toString();
    }
}
