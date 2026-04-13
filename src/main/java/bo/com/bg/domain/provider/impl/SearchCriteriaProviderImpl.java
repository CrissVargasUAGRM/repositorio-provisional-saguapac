package bo.com.bg.domain.provider.impl;

import bo.com.bg.commons.db.QueryManager;
import bo.com.bg.commons.exceptions.ProviderException;
import bo.com.bg.commons.enums.ErrorMessage;
import bo.com.bg.domain.config.ProviderConfigSql;
import bo.com.bg.domain.provider.SearchCriteriaProvider;
import bo.com.bg.domain.provider.response.SearchCriteriaConsultationResponse;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class SearchCriteriaProviderImpl implements SearchCriteriaProvider {

    private final QueryManager<?> queryManager;
    private final ProviderConfigSql config;

    public SearchCriteriaProviderImpl(QueryManager<?> queryManager, ProviderConfigSql config) {
        this.queryManager = queryManager;
        this.config = config;
    }

    @Override
    public List<SearchCriteriaConsultationResponse> getCriteria(int serviceId) throws ProviderException {
        String sql = config.getQueryGetCriteria();
        if (sql == null || sql.isBlank()) {
            throw new ProviderException(
                    ErrorMessage.CRITERIA_SEARCH_ERROR.getMsg(),
                    ErrorMessage.CRITERIA_SEARCH_ERROR.getCod(),
                    HttpStatus.NOT_ACCEPTABLE);
        }

        Map<String, Object> params = Map.of("serviceId", serviceId);
        List<SearchCriteriaConsultationResponse> result =
                queryManager.executeQueryForList(sql, params, SearchCriteriaConsultationResponse.class);

        if (result == null || result.isEmpty()) {
            throw new ProviderException(
                    ErrorMessage.CRITERIA_SEARCH_ERROR.getMsg(),
                    ErrorMessage.CRITERIA_SEARCH_ERROR.getCod(),
                    HttpStatus.NOT_ACCEPTABLE);
        }

        return Collections.unmodifiableList(result);
    }
}
