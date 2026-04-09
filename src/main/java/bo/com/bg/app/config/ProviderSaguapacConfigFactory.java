package bo.com.bg.app.config;

import bo.com.bg.domain.config.ProviderSaguapacConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RefreshScope
public class ProviderSaguapacConfigFactory {

    @Value("${saguapac.provider.base-url}")
    private String baseUrl;

    @Value("${saguapac.provider.endpoint}")
    private String endpoint;

    @Value("${saguapac.provider.login}")
    private String login;

    @Value("${saguapac.provider.password}")
    private String password;

    @Value("${saguapac.provider.sucursal}")
    private String sucursal;

    @Value("${saguapac.provider.cajero}")
    private String cajero;

    @Value("${saguapac.provider.password-cajero}")
    private String passwordCajero;

    @Value("${saguapac.provider.timeout}")
    private int timeout;

    @Value("${saguapac.provider.context-path-clients}")
    private String contextPathClients;

    @Value("${saguapac.provider.is-mock-enabled:false}")
    private boolean isMockEnabled;

    @Bean
    @RefreshScope
    public ProviderSaguapacConfig providerSaguapacConfig() {
        return ProviderSaguapacConfig.builder()
                .baseUrl(baseUrl)
                .endpoint(endpoint)
                .login(login)
                .password(password)
                .sucursal(sucursal)
                .cajero(cajero)
                .passwordCajero(passwordCajero)
                .timeout(timeout)
                .contextPathClients(contextPathClients)
                .mockEnabled(isMockEnabled)
                .build();
    }
}