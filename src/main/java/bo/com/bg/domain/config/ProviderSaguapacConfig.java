package bo.com.bg.domain.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    /** Path del recurso facturas/cliente pendientes (Saguapac). */
    private String contextPathClients;
    private boolean mockEnabled;

    /**
     * URL base del GET de facturas pendientes: {@code baseUrl} + {@code contextPathClients}.
     */
    public String urlInvoicePendingSaguapac() {
        String base = baseUrl != null ? baseUrl.replaceAll("/+$", "") : "";
        String path = contextPathClients != null ? contextPathClients : "";
        if (path.isEmpty()) {
            return base;
        }
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        return base + path;
    }
}
