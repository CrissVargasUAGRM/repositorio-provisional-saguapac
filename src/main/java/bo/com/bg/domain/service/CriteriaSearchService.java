package bo.com.bg.domain.service;

import bo.com.bg.app.request.SearchCriteriaRequest;
import bo.com.bg.app.response.SearchCriteriaResponse;

public interface CriteriaSearchService {

    SearchCriteriaResponse obtainSearchCriteria(SearchCriteriaRequest payload);
}
