package bo.com.bg.domain.service.impl;

import bo.com.bg.app.request.SaguapacAuthRequest;
import bo.com.bg.app.response.SaguapacTokenResponse;
import bo.com.bg.domain.config.ProviderSaguapacConfig;
import bo.com.bg.domain.connector.AuthenticationConnector;
import bo.com.bg.domain.connector.request.SaguapacLoginRequest;
import bo.com.bg.domain.service.SaguapacAuthService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SaguapacAuthServiceImpl implements SaguapacAuthService {

    private static final Logger LOG = LoggerFactory.getLogger(SaguapacAuthServiceImpl.class);

    private final AuthenticationConnector authenticationConnector;

    private final ProviderSaguapacConfig config;

    @Override
    public SaguapacTokenResponse authenticate(SaguapacAuthRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("406 - request requerido");
        }
        try {
            SaguapacLoginRequest loginRequest = SaguapacLoginRequest.builder()
                    .login(config.getLogin())
                    .pass(config.getPassword())
                    .sucursal(config.getSucursal())
                    .cajero(config.getCajero())
                    .passCajero(config.getPasswordCajero())
                    .build();
            String token = authenticationConnector.getToken(loginRequest);
            return SaguapacTokenResponse.builder()
                    .token(token)
                    .valido(true)
                    .mensaje(null)
                    .build();
        } catch (Exception e) {
            LOG.error("Error en autenticacion Saguapac", e);
            return SaguapacTokenResponse.builder()
                    .token(null)
                    .valido(false)
                    .mensaje(e.getMessage())
                    .build();
        }
    }
}
