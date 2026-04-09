package bo.com.bg.domain.provider.request;

/**
 * DTO de consulta hacia proveedores externos (cobertura excluida por reglas JaCoCo).
 */
public record ProviderQuery(String externalId) {

    public ProviderQuery {
        externalId = externalId == null ? "" : externalId;
    }
}
