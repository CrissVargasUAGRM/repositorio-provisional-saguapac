package bo.com.bg.domain.connector;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import bo.com.bg.commons.exceptions.ProviderException;
import bo.com.bg.domain.config.ProviderSaguapacConfig;
import bo.com.bg.domain.connector.exception.SaguapacIntegrationException;
import bo.com.bg.domain.connector.impl.AuthenticationConnectorImpl;
import bo.com.bg.domain.connector.request.SaguapacLoginRequest;
import bo.com.bg.domain.connector.response.SaguapacLoginResponse;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class SaguapacConnectorImplTest {

    @Mock
    private ProviderSaguapacConfig config;

    @BeforeEach
    void setUp() {
        when(config.getBaseUrl()).thenReturn("http://localhost");
        when(config.getEndpoint()).thenReturn("/api/login");
    }

    @Test
    void getToken_shouldReturnToken_whenResponseIs200() throws Exception {
        TestConnector connector = new TestConnector(config);
        connector.loginResponse = SaguapacLoginResponse.builder().authorization("Bearer token-ok").build();

        assertThat(connector.getToken(sampleRequest())).isEqualTo("Bearer token-ok");
    }

    @Test
    void getToken_shouldThrowException_whenResponseIs401() {
        TestConnector connector = new TestConnector(config);
        connector.postError = new ProviderException("credencial invalida", "SAGUAPAC_AUTH", HttpStatus.UNAUTHORIZED);

        assertThatThrownBy(() -> connector.getToken(sampleRequest()))
                .isInstanceOf(SaguapacIntegrationException.class)
                .satisfies(t -> {
                    SaguapacIntegrationException e = (SaguapacIntegrationException) t;
                    assertThat(e.getStatusCode()).isEqualTo(401);
                    assertThat(e.getMessage()).contains("credencial invalida");
                });
    }

    @Test
    void getToken_shouldThrowException_whenNetworkFails() {
        TestConnector connector = new TestConnector(config);
        connector.postError = new ProviderException("connection reset", "ERROR", HttpStatus.INTERNAL_SERVER_ERROR);

        assertThatThrownBy(() -> connector.getToken(sampleRequest()))
                .isInstanceOf(SaguapacIntegrationException.class)
                .hasMessageContaining("connection reset");
    }

    private static SaguapacLoginRequest sampleRequest() {
        return SaguapacLoginRequest.builder()
                .login("l")
                .pass("p")
                .sucursal("s")
                .cajero("c")
                .passCajero("pc")
                .build();
    }

    /**
     * Sustituye {@code post} de {@link CustomServiceConnector} sin exponer el método protegido desde el test.
     */
    private static final class TestConnector extends AuthenticationConnectorImpl {

        SaguapacLoginResponse loginResponse;
        ProviderException postError;

        TestConnector(ProviderSaguapacConfig config) {
            super(config);
        }

        @Override
        @SuppressWarnings("unchecked")
        protected <T> T post(
                Class<T> returnType,
                String url,
                Object payload,
                Map<String, ?> params,
                HttpHeaders headers)
                throws ProviderException {
            if (postError != null) {
                throw postError;
            }
            return (T) loginResponse;
        }
    }
}
