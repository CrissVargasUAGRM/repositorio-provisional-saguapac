package bo.com.bg.domain.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Propiedades SQL (no {@code final}) para permitir proxy CGLIB con {@code @RefreshScope} en el factory.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProviderConfigSql {

    private String queryGetCriteria;
    private String schemaGanadero;
    private String packageSrvName;
    private String spSrvInsertName;
    private int allowedCriteriaServiceId;
}
