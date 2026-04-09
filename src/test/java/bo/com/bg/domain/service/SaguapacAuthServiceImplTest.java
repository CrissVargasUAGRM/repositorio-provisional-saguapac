package bo.com.bg.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import bo.com.bg.app.request.SaguapacAuthRequest;
import bo.com.bg.app.response.SaguapacTokenResponse;
import bo.com.bg.domain.connector.AuthenticationConnector;
import bo.com.bg.domain.service.impl.SaguapacAuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SaguapacAuthServiceImplTest {

    @Mock
    private AuthenticationConnector authenticationConnector;

    @InjectMocks
    private SaguapacAuthServiceImpl service;

    @Test
    void authenticate_shouldReturnToken_whenConnectorSucceeds() {
        when(authenticationConnector.getToken(any())).thenReturn("tok");

        SaguapacTokenResponse r = service.authenticate(sampleRequest());
        assertThat(r.isValido()).isTrue();
        assertThat(r.getToken()).isEqualTo("tok");
        assertThat(r.getMensaje()).isNull();
    }

    @Test
    void authenticate_shouldReturnInvalid_whenConnectorThrows() {
        when(authenticationConnector.getToken(any())).thenThrow(new RuntimeException("fallo red"));

        SaguapacTokenResponse r = service.authenticate(sampleRequest());
        assertThat(r.isValido()).isFalse();
        assertThat(r.getMensaje()).contains("fallo red");
    }

    @Test
    void authenticate_shouldThrowIllegalArgumentException_whenRequestIsNull() {
        assertThatThrownBy(() -> service.authenticate(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("406");
    }

    private static SaguapacAuthRequest sampleRequest() {
        return SaguapacAuthRequest.builder()
                .login("l")
                .password("p")
                .sucursal("s")
                .cajero("c")
                .passwordCajero("pc")
                .build();
    }
}
