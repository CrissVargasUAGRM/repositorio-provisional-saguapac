package bo.com.bg.app.config;

import bo.com.bg.domain.config.ProviderConfigSql;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RefreshScope
public class ProviderConfigSqlFactory {

    @Value("${provider.sql.query-get-criteria}")
    private String queryGetCriteria;

    @Value("${provider.sql.schema-ganadero}")
    private String schemaGanadero;

    @Value("${provider.sql.package-srv-name}")
    private String packageSrvName;

    @Value("${provider.sql.sp-srv-insert-name}")
    private String spSrvInsertName;

    @Value("${provider.sql.allowed-criteria-service-id:705}")
    private int allowedCriteriaServiceId;

    @Bean
    @RefreshScope
    public ProviderConfigSql providerConfigSql() {
        return ProviderConfigSql.builder()
                .queryGetCriteria(queryGetCriteria)
                .schemaGanadero(schemaGanadero)
                .packageSrvName(packageSrvName)
                .spSrvInsertName(spSrvInsertName)
                .allowedCriteriaServiceId(allowedCriteriaServiceId)
                .build();
    }
}
