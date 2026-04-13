package bo.com.bg.domain.service.impl;

import bo.com.bg.app.request.SearchCriteriaRequest;
import bo.com.bg.app.request.SearchCriteriaRequestData;
import bo.com.bg.app.response.SearchCriteriaResponse;
import bo.com.bg.app.response.SearchCriteriaResponseCriteria;
import bo.com.bg.app.response.SearchCriteriaResponseCriteriaItem;
import bo.com.bg.commons.enums.ErrorMessage;
import bo.com.bg.commons.exceptions.ProviderException;
import bo.com.bg.domain.config.ProviderConfigSql;
import bo.com.bg.domain.provider.SearchCriteriaProvider;
import bo.com.bg.domain.provider.response.SearchCriteriaConsultationResponse;
import bo.com.bg.domain.service.CriteriaSearchService;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CriteriaSearchServiceImpl implements CriteriaSearchService {

    private static final Logger log = LoggerFactory.getLogger(CriteriaSearchServiceImpl.class);

    private final SearchCriteriaProvider searchCriteriaProvider;
    private final ProviderConfigSql providerConfigSql;

    public CriteriaSearchServiceImpl(SearchCriteriaProvider searchCriteriaProvider, ProviderConfigSql providerConfigSql) {
        this.searchCriteriaProvider = searchCriteriaProvider;
        this.providerConfigSql = providerConfigSql;
    }

    @Override
    public SearchCriteriaResponse obtainSearchCriteria(SearchCriteriaRequest payload) {
        validatePayload(payload);
        validateServiceId(payload);

        SearchCriteriaRequestData data = payload.getData();
        List<SearchCriteriaConsultationResponse> getParams = getParams(data);

        Set<String> abreviaturasUnicas = new HashSet<>();
        List<SearchCriteriaResponseCriteria> criteriaList = new ArrayList<>();

        List<SearchCriteriaConsultationResponse> criteriaFullList = getParams.stream()
                .filter(item -> {
                    String abbr = item.getAbbreviation();
                    String key = abbr != null && !abbr.isBlank() ? abbr : "id:" + item.getIdentifier();
                    if (!abreviaturasUnicas.contains(key)) {
                        abreviaturasUnicas.add(key);
                        return true;
                    }
                    return false;
                })
                .toList();

        criteriaFullList.forEach(fullItem -> {
            String abbrKey = fullItem.getAbbreviation() != null && !fullItem.getAbbreviation().isBlank()
                    ? fullItem.getAbbreviation()
                    : "id:" + fullItem.getIdentifier();
            List<SearchCriteriaConsultationResponse> listByAbbreviation = getParams.stream()
                    .filter(item -> {
                        String k = item.getAbbreviation() != null && !item.getAbbreviation().isBlank()
                                ? item.getAbbreviation()
                                : "id:" + item.getIdentifier();
                        return k.equals(abbrKey);
                    })
                    .toList();

            List<SearchCriteriaResponseCriteriaItem> fieldList = new ArrayList<>();

            fieldList.add(SearchCriteriaResponseCriteriaItem.builder()
                    .label(fullItem.getLabelCriteria())
                    .identifier(fullItem.getCodeExterno())
                    .description(fullItem.getDescription())
                    .abbreviation(fullItem.getAbbreviation())
                    .required(Boolean.parseBoolean(valueMandatory(fullItem.getIsMandatory())))
                    .size(fullItem.getMaximumLength())
                    .service(fullItem.getServiceName())
                    .type(fullItem.getDataTypeCode())
                    .group(fullItem.getIdentifier())
                    .alias(fullItem.getAlias())
                    .order(fullItem.getOrdinal())
                    .actionCode(fullItem.getCodAction())
                    .value(valueDefault(fullItem.getValueDefault()))
                    .serviceType(null)
                    .operationCode(null)
                    .visible(Boolean.FALSE)
                    .build());

            List<SearchCriteriaResponseCriteriaItem> childFields = listByAbbreviation.stream()
                    .map(item -> {
                        if (Objects.nonNull(item.getCriterionCodeChild())) {
                            SearchCriteriaConsultationResponse child = getParams.stream()
                                    .filter(c -> Objects.equals(c.getIdentifier(), item.getCriterionCodeChild()))
                                    .findFirst()
                                    .orElse(null);

                            if (child == null) {
                                return null;
                            }
                            return SearchCriteriaResponseCriteriaItem.builder()
                                    .label(child.getLabelCriteria())
                                    .identifier(child.getCodeExterno())
                                    .description(child.getDescription())
                                    .group(child.getIdentifier())
                                    .abbreviation(child.getAbbreviation())
                                    .required(Boolean.parseBoolean(valueMandatory(child.getIsMandatory())))
                                    .size(child.getMaximumLength())
                                    .type(child.getDataTypeCode())
                                    .service(child.getServiceName())
                                    .alias(child.getAlias())
                                    .order(child.getOrdinal())
                                    .actionCode(child.getCodAction())
                                    .value(valueDefault(child.getValueDefault()))
                                    .serviceType(null)
                                    .operationCode(null)
                                    .visible(Boolean.FALSE)
                                    .build();
                        }
                        return null;
                    })
                    .filter(Objects::nonNull)
                    .toList();

            fieldList.addAll(childFields);

            criteriaList.add(SearchCriteriaResponseCriteria.builder()
                    .description(fullItem.getDescription())
                    .group(fullItem.getIdentifier())
                    .fields(fieldList)
                    .build());
        });

        log.debug("Criterios armados para serviceId={}", data.getServiceId());

        return SearchCriteriaResponse.builder()
                .serviceId(payload.getData().getServiceId())
                .criteria(criteriaList)
                .build();
    }

    private String valueMandatory(String value) {
        return value != null && value.equalsIgnoreCase("N") ? "false" : "true";
    }

    private String valueDefault(String value) {
        return value == null ? "0" : value;
    }

    private void validatePayload(SearchCriteriaRequest payload) {
        if (payload == null || payload.getData() == null) {
            throw new ProviderException(
                    ErrorMessage.CRITERIA_SEARCH_ERROR.getMsg(),
                    ErrorMessage.CRITERIA_SEARCH_ERROR.getCod(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    private void validateServiceId(SearchCriteriaRequest payload) {
        Integer sid = payload.getData().getServiceId();
        if (sid == null || sid != providerConfigSql.getAllowedCriteriaServiceId()) {
            throw new ProviderException(
                    ErrorMessage.VALIDATION_ERROR.getMsg(),
                    ErrorMessage.VALIDATION_ERROR.getCod(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    private List<SearchCriteriaConsultationResponse> getParams(SearchCriteriaRequestData data) {
        return this.searchCriteriaProvider.getCriteria(data.getServiceId());
    }
}
