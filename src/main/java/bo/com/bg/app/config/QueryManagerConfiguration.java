package bo.com.bg.app.config;

import bo.com.bg.commons.db.QueryManager;
import bo.com.bg.commons.log.LogPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Constructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
public class QueryManagerConfiguration {

    @Bean
    public LogPrinter logPrinter() throws ReflectiveOperationException {
        Constructor<LogPrinter> ctor = LogPrinter.class.getDeclaredConstructor();
        ctor.setAccessible(true);
        return ctor.newInstance();
    }

    @Bean
    public QueryManager<?> queryManager(
            NamedParameterJdbcTemplate namedParameterJdbcTemplate,
            ObjectMapper objectMapper,
            LogPrinter logPrinter) {
        return new QueryManager<>(namedParameterJdbcTemplate, objectMapper, logPrinter);
    }
}
