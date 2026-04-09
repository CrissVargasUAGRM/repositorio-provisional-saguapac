package bo.com.bg.domain.connector.impl;

import bo.com.bg.commons.config.CustomServiceConnector;
import bo.com.bg.commons.exceptions.ProviderException;
import bo.com.bg.domain.config.ProviderSaguapacConfig;
import bo.com.bg.domain.connector.AuthenticationConnector;
import bo.com.bg.domain.connector.exception.SaguapacIntegrationException;
import bo.com.bg.domain.connector.request.SaguapacLoginRequest;
import bo.com.bg.domain.connector.response.SaguapacErrorResponse;
import bo.com.bg.domain.connector.response.SaguapacLoginResponse;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;

@Service
public class AuthenticationConnectorImpl extends CustomServiceConnector implements AuthenticationConnector {

    private static final Logger LOG = LogManager.getLogger(AuthenticationConnectorImpl.class);

    private final ProviderSaguapacConfig config;

    public AuthenticationConnectorImpl(ProviderSaguapacConfig config) {
        this.config = config;
    }

    @Override
    public String getToken(SaguapacLoginRequest request) {
        requireConfig();
        String url = config.getBaseUrl() + config.getEndpoint();
        LOG.debug("POST Saguapac login url={}", url);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        SaguapacLoginResponse response;
        try {
            response = post(SaguapacLoginResponse.class, url, request, null, headers);
        } catch (ProviderException e) {
            HttpStatus status = e.getStatus();
            int code = status != null ? status.value() : HttpStatus.INTERNAL_SERVER_ERROR.value();
            String msg = e.getMessage() != null ? e.getMessage() : "";
            throw new SaguapacIntegrationException(code, msg, e);
        }
        if (response == null || response.getAuthorization() == null || response.getAuthorization().isBlank()) {
            throw new ProviderException(
                    "Respuesta sin campo authorization", "ERR1000", HttpStatus.NOT_ACCEPTABLE);
        }
        return response.getAuthorization();
    }

    @Override
    protected <T> T handleRestException(HttpStatusCodeException exception) throws ProviderException {
        if (exception.getStatusCode().value() == HttpStatus.UNAUTHORIZED.value()) {
            String body = exception.getResponseBodyAsString(StandardCharsets.UTF_8);
            String msg = messageFromErrorBody(body);
            throw new ProviderException(msg, "SAGUAPAC_AUTH", HttpStatus.UNAUTHORIZED);
        }
        return super.handleRestException(exception);
    }

    private void requireConfig() {
        if (config.getBaseUrl() == null || config.getBaseUrl().isBlank()
                || config.getEndpoint() == null || config.getEndpoint().isBlank()) {
            throw new IllegalStateException("saguapac.provider.base-url y endpoint deben estar configurados");
        }
    }

    private String messageFromErrorBody(String responseBody) {
        if (responseBody == null || responseBody.isBlank()) {
            return "";
        }
        try {
            SaguapacErrorResponse err = mapper.readValue(responseBody, SaguapacErrorResponse.class);
            if (err.getErrors() != null && !err.getErrors().isEmpty()) {
                return err.getErrors().stream().map(String::trim).collect(Collectors.joining("; "));
            }
        } catch (Exception parseEx) {
            LOG.debug("No se pudo parsear SaguapacErrorResponse, se usa body crudo");
        }
        return responseBody;
    }
}
