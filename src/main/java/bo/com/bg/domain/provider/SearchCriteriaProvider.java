package bo.com.bg.domain.provider;

import bo.com.bg.commons.exceptions.ProviderException;
import bo.com.bg.domain.provider.response.SearchCriteriaConsultationResponse;
import java.util.List;

public interface SearchCriteriaProvider {

    List<SearchCriteriaConsultationResponse> getCriteria(int serviceId) throws ProviderException;
}
