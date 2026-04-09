package bo.com.bg.domain.connector.impl;

import bo.com.bg.app.request.SaguapacAuthRequest;
import bo.com.bg.app.response.SaguapacTokenResponse;
import bo.com.bg.commons.config.CustomServiceConnector;
import bo.com.bg.commons.exceptions.ProviderException;
import bo.com.bg.domain.config.ProviderSaguapacConfig;
import bo.com.bg.domain.connector.InvoicePendingConnector;
import bo.com.bg.domain.connector.response.InvoicePendingResponseDTO;
import bo.com.bg.domain.service.SaguapacAuthService;
import java.net.URI;
import java.util.Collections;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class InvoicePendingConnectorImpl extends CustomServiceConnector implements InvoicePendingConnector {

    private static final Logger LOG = LogManager.getLogger(InvoicePendingConnectorImpl.class);

    private final ProviderSaguapacConfig config;
    private final SaguapacAuthService saguapacAuthService;
    private final URI invoicePendingUri;

    public InvoicePendingConnectorImpl(ProviderSaguapacConfig config, SaguapacAuthService saguapacAuthService) {
        this.config = config;
        this.saguapacAuthService = saguapacAuthService;
        this.invoicePendingUri = URI.create(config.urlInvoicePendingSaguapac());
    }

    @Override
    public InvoicePendingResponseDTO getPendingInvoices(String codigoSocio) throws ProviderException {
        requireConfig();
        String token = resolveToken();
        UriComponentsBuilder builder =
                UriComponentsBuilder.fromUri(invoicePendingUri).queryParam("codigoSocio", codigoSocio);
        String url = builder.build().encode().toUriString();
        LOG.debug("GET Saguapac facturas pendientes url={}", url);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set(HttpHeaders.AUTHORIZATION, token);

        return getRequest(InvoicePendingResponseDTO.class, url, null, headers);
    }

    private void requireConfig() {
        String url = config.urlInvoicePendingSaguapac();
        if (url == null || url.isBlank()) {
            throw new IllegalStateException("saguapac.provider.base-url y context-path-clients deben estar configurados");
        }
    }

    private String resolveToken() {
        SaguapacTokenResponse auth = saguapacAuthService.authenticate(internalAuthRequest());
        if (auth == null || !auth.isValido() || auth.getToken() == null || auth.getToken().isBlank()) {
            throw new IllegalStateException("No se pudo obtener token de integración Saguapac");
        }
        return auth.getToken().trim();
    }

    /**
     * El servicio de autenticación usa credenciales de {@link ProviderSaguapacConfig}; el body solo cumple validación.
     */
    private static SaguapacAuthRequest internalAuthRequest() {
        return SaguapacAuthRequest.builder()
                .login("-")
                .password("-")
                .sucursal("-")
                .cajero("-")
                .passwordCajero("-")
                .build();
    }
}
