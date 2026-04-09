package bo.com.bg.domain.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

/**
 * Propiedades {@code saguapac.provider.*} definidas en configuración externa (sin valores por defecto en código).
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProviderSaguapacConfig {

    private String baseUrl;
    private String endpoint;
    private int timeout;
    private String login;
    private String password;
    private String sucursal;
    private String cajero;
    private String passwordCajero;
}
