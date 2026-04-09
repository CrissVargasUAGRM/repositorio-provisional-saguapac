package bo.com.bg.domain.connector.exception;

/**
 * Error de integración HTTP con Saguapac (status del proveedor o cuerpo de error parseado).
 */
public class SaguapacIntegrationException extends RuntimeException {

    private final int statusCode;

    public SaguapacIntegrationException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public SaguapacIntegrationException(int statusCode, String message, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
