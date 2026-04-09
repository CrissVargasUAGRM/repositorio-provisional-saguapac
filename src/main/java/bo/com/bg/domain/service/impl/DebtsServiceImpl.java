package bo.com.bg.domain.service.impl;

import bo.com.bg.app.request.DebtsRequest;
import bo.com.bg.app.response.DebtResponseField;
import bo.com.bg.app.response.DebtsDeudaGroup;
import bo.com.bg.app.response.DebtsResponse;
import bo.com.bg.commons.enums.ErrorMessage;
import bo.com.bg.commons.exceptions.ProviderException;
import bo.com.bg.domain.config.ProviderSaguapacConfig;
import bo.com.bg.domain.connector.InvoicePendingConnector;
import bo.com.bg.domain.connector.response.InvoicePendingResponseDTO;
import bo.com.bg.domain.connector.response.InvoicePendingResponseDTO.ElectronicInvoiceDto;
import bo.com.bg.domain.connector.response.InvoicePendingResponseDTO.PartnerDto;
import bo.com.bg.domain.mocks.InvoicePendingResponseMock;
import bo.com.bg.domain.service.DebtsService;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class DebtsServiceImpl implements DebtsService {

    private static final Logger LOG = LoggerFactory.getLogger(DebtsServiceImpl.class);

    private static final String FILTER_CODIGO_CONEXION = "codigoConexion";

    private final InvoicePendingConnector invoicePendingConnector;
    private final ProviderSaguapacConfig config;

    public DebtsServiceImpl(InvoicePendingConnector invoicePendingConnector, ProviderSaguapacConfig config) {
        this.invoicePendingConnector = invoicePendingConnector;
        this.config = config;
    }

    @Override
    public DebtsResponse getDebts(DebtsRequest payload) {
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

        String codigoConexion = extractCodigoConexion(payload);
        if (codigoConexion == null || codigoConexion.isBlank()) {
            throw new IllegalArgumentException("406 - valor de codigoConexion requerido");
        }
        codigoConexion = sanitize(codigoConexion);

        Integer serviceId = payload.getData().getServiceId();

        InvoicePendingResponseDTO providerResponse;
        try {
            if (config.isMockEnabled()) {
                providerResponse = InvoicePendingResponseMock.sample();
            } else {
                providerResponse = invoicePendingConnector.getPendingInvoices(codigoConexion);
            }
        } catch (ProviderException e) {
            LOG.warn("Error proveedor Saguapac deudas: {}", e.getMessage());
            throw new ProviderException(
                    ErrorMessage.ERROR_SERVICE_EXTERNO.getMsg(),
                    ErrorMessage.ERROR_SERVICE_EXTERNO.getCod(),
                    HttpStatus.NOT_ACCEPTABLE);
        } catch (IllegalStateException e) {
            throw new IllegalArgumentException("406 - " + e.getMessage());
        }

        return buildResponse(providerResponse, serviceId);
    }

    private static String extractCodigoConexion(DebtsRequest payload) {
        return payload.getData().getFiltro().stream()
                .filter(Objects::nonNull)
                .filter(f -> FILTER_CODIGO_CONEXION.equalsIgnoreCase(f.getLabel()))
                .map(DebtsRequest.DebtsFiltroItem::getValue)
                .findFirst()
                .orElse(null);
    }

    private static String sanitize(String raw) {
        return Jsoup.clean(raw, Safelist.basic());
    }

    private static DebtsResponse buildResponse(InvoicePendingResponseDTO dto, Integer serviceId) {
        if (dto == null || dto.getPartner() == null) {
            return DebtsResponse.builder()
                    .serviceId(serviceId)
                    .deudas(Collections.emptyList())
                    .build();
        }

        PartnerDto partner = dto.getPartner();
        List<ElectronicInvoiceDto> invoices = partner.getElectronicInvoices();
        if (invoices == null || invoices.isEmpty()) {
            return DebtsResponse.builder()
                    .serviceId(serviceId)
                    .deudas(Collections.emptyList())
                    .build();
        }

        List<DebtsDeudaGroup> groups = new ArrayList<>();
        for (ElectronicInvoiceDto inv : invoices) {
            if (inv == null) {
                continue;
            }
            String debtUuid = UUID.randomUUID().toString();
            groups.add(
                    DebtsDeudaGroup.builder().deuda(buildFieldsForInvoice(inv, partner, debtUuid)).build());
        }

        return DebtsResponse.builder().serviceId(serviceId).deudas(groups).build();
    }

    private static List<DebtResponseField> buildFieldsForInvoice(
            ElectronicInvoiceDto inv, PartnerDto partner, String debtUuid) {
        List<DebtResponseField> fields = new ArrayList<>();
        PeriodParts parts = parsePeriodParts(inv.getPeriod());

        fields.add(debtField(debtUuid, "idFactura", "idFactura", sanitizeNullable(inv.getInvoiceId()), "A"));
        fields.add(debtField(debtUuid, "periodo", "periodo", parts.yearOnly(), "N"));
        fields.add(debtField(debtUuid, "total", "total", sanitizeNullable(inv.getTotalLabel()), "A"));
        fields.add(debtField(
                debtUuid,
                "importeTotal",
                "importeTotal",
                inv.getTotalAmount() != null ? String.valueOf(inv.getTotalAmount()) : "",
                "N"));
        fields.add(debtField(
                debtUuid,
                "bloqueadoPorQR",
                "bloqueadoPorQR",
                sanitizeNullable(inv.getBlockedByQr()),
                "A"));
        fields.add(debtField(
                debtUuid,
                "idConexion",
                "idConexion",
                sanitizeNullable(partner.getConnectionId()),
                "A"));
        fields.add(debtField(
                debtUuid,
                "codigoConexion",
                "codigoConexion",
                sanitizeNullable(partner.getConnectionCode()),
                "A"));
        fields.add(debtField(debtUuid, "gestion", "gestion", parts.gestion(), "N"));
        fields.add(debtField(debtUuid, "mesPeriodo", "mesPeriodo", parts.monthOnly(), "N"));
        fields.add(debtField(debtUuid, "anioPeriodo", "anioPeriodo", parts.yearOnly(), "N"));

        return fields;
    }

    /**
     * A partir de {@code periodo} tipo {@code yyyy-MM}: año en {@code periodo}/{@code anioPeriodo}/{@code gestion},
     * mes en {@code mesPeriodo}. Si el formato no coincide, se devuelven cadenas vacías.
     */
    private static PeriodParts parsePeriodParts(String period) {
        if (period == null || period.isBlank()) {
            return PeriodParts.EMPTY;
        }
        try {
            YearMonth ym = YearMonth.parse(period.trim());
            String y = String.valueOf(ym.getYear());
            String m = String.format("%02d", ym.getMonthValue());
            return new PeriodParts(y, m);
        } catch (DateTimeParseException e) {
            return PeriodParts.EMPTY;
        }
    }

    private static DebtResponseField debtField(
            String debtUuid, String code, String label, String value, String tipoDato) {
        String v = value != null ? value : "";
        return DebtResponseField.builder()
                .label(label)
                .value(v)
                .mandatory(false)
                .editable("N")
                .description(null)
                .code(code)
                .grupo("deuda")
                .indice(debtUuid)
                .tipoDato(tipoDato)
                .build();
    }

    private static String sanitizeNullable(String raw) {
        if (raw == null) {
            return "";
        }
        return sanitize(raw);
    }

    private record PeriodParts(String yearOnly, String monthOnly) {
        static final PeriodParts EMPTY = new PeriodParts("", "");

        String gestion() {
            return yearOnly;
        }
    }
}
