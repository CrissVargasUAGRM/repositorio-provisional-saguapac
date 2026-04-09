package bo.com.bg.domain.connector.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import bo.com.bg.app.request.SaguapacAuthRequest;
import bo.com.bg.app.response.SaguapacTokenResponse;
import bo.com.bg.commons.exceptions.ProviderException;
import bo.com.bg.domain.config.ProviderSaguapacConfig;
import bo.com.bg.domain.connector.response.InvoicePendingResponseDTO;
import bo.com.bg.domain.service.SaguapacAuthService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class InvoicePendingConnectorImplTest {

    @Mock
    private ProviderSaguapacConfig config;

    @Mock
    private SaguapacAuthService saguapacAuthService;

    @BeforeEach
    void setUp() {
        when(config.urlInvoicePendingSaguapac()).thenReturn("http://localhost:8090/api/pendientes");
    }

    @Test
    void getPendingInvoices_appendsCodigoSocioQuery_andReturnsBody() throws Exception {
        when(saguapacAuthService.authenticate(any(SaguapacAuthRequest.class)))
                .thenReturn(SaguapacTokenResponse.builder().valido(true).token("Bearer tok").build());

        TestConnector connector = new TestConnector(config, saguapacAuthService);
        prepareMapper(connector);

        InvoicePendingResponseDTO body = InvoicePendingResponseDTO.builder()
                .partner(InvoicePendingResponseDTO.PartnerDto.builder()
                        .connectionId("1")
                        .build())
                .build();
        connector.responseToReturn = body;

        InvoicePendingResponseDTO result = connector.getPendingInvoices("12345");

        assertThat(result).isSameAs(body);
        assertThat(connector.lastUrl).contains("codigoSocio=12345");
        assertThat(connector.lastHeaders).isNotNull();
        assertThat(connector.lastHeaders.getFirst(HttpHeaders.AUTHORIZATION)).isEqualTo("Bearer tok");
    }

    @Test
    void getPendingInvoices_propagatesProviderExceptionFromGet() throws Exception {
        when(saguapacAuthService.authenticate(any(SaguapacAuthRequest.class)))
                .thenReturn(SaguapacTokenResponse.builder().valido(true).token("Bearer t").build());

        TestConnector connector = new TestConnector(config, saguapacAuthService);
        prepareMapper(connector);
        connector.errorToThrow = new ProviderException("x", "E", HttpStatus.BAD_GATEWAY);

        assertThatThrownBy(() -> connector.getPendingInvoices("1")).isInstanceOf(ProviderException.class);
    }

    private static void prepareMapper(InvoicePendingConnectorImpl connector) {
        ObjectMapper mapper = new ObjectMapper()
                .findAndRegisterModules()
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false);
        ReflectionTestUtils.setField(connector, "mapper", mapper);
    }

    private static final class TestConnector extends InvoicePendingConnectorImpl {

        InvoicePendingResponseDTO responseToReturn;
        ProviderException errorToThrow;
        String lastUrl;
        HttpHeaders lastHeaders;

        TestConnector(ProviderSaguapacConfig c, SaguapacAuthService a) {
            super(c, a);
        }

        @Override
        protected <T> T getRequest(
                Class<T> returnType,
                String url,
                Map<String, ?> params,
                HttpHeaders headers)
                throws ProviderException {
            this.lastUrl = url;
            this.lastHeaders = headers;
            if (errorToThrow != null) {
                throw errorToThrow;
            }
            return returnType.cast(responseToReturn);
        }
    }
}
