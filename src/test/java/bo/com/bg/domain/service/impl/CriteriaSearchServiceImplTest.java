package bo.com.bg.domain.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import bo.com.bg.app.request.SearchCriteriaRequest;
import bo.com.bg.app.request.SearchCriteriaRequestData;
import bo.com.bg.app.response.SearchCriteriaResponse;
import bo.com.bg.commons.exceptions.ProviderException;
import bo.com.bg.domain.config.ProviderConfigSql;
import bo.com.bg.domain.provider.SearchCriteriaProvider;
import bo.com.bg.domain.provider.response.SearchCriteriaConsultationResponse;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CriteriaSearchServiceImplTest {

    @Mock
    private SearchCriteriaProvider searchCriteriaProvider;

    @Mock
    private ProviderConfigSql providerConfigSql;

    private CriteriaSearchServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new CriteriaSearchServiceImpl(searchCriteriaProvider, providerConfigSql);
        when(providerConfigSql.getAllowedCriteriaServiceId()).thenReturn(705);
    }

    @Test
    void obtainSearchCriteria_rejectsNullPayload() {
        assertThatThrownBy(() -> service.obtainSearchCriteria(null)).isInstanceOf(ProviderException.class);
    }

    @Test
    void obtainSearchCriteria_rejectsNullData() {
        SearchCriteriaRequest req = SearchCriteriaRequest.builder().data(null).build();
        assertThatThrownBy(() -> service.obtainSearchCriteria(req)).isInstanceOf(ProviderException.class);
    }

    @Test
    void obtainSearchCriteria_rejectsWrongServiceId() {
        SearchCriteriaRequest req = SearchCriteriaRequest.builder()
                .data(SearchCriteriaRequestData.builder().serviceId(999).build())
                .build();
        assertThatThrownBy(() -> service.obtainSearchCriteria(req))
                .isInstanceOf(ProviderException.class)
                .satisfies(ex -> assertThat(((ProviderException) ex).getStatus()).isEqualTo(HttpStatus.BAD_REQUEST));
    }

    @Test
    void obtainSearchCriteria_rejectsNullServiceId() {
        SearchCriteriaRequest req = SearchCriteriaRequest.builder()
                .data(SearchCriteriaRequestData.builder().serviceId(null).build())
                .build();
        assertThatThrownBy(() -> service.obtainSearchCriteria(req))
                .isInstanceOf(ProviderException.class)
                .satisfies(ex -> assertThat(((ProviderException) ex).getStatus()).isEqualTo(HttpStatus.BAD_REQUEST));
    }

    @Test
    void obtainSearchCriteria_skipsChildWhenNotFound() {
        SearchCriteriaConsultationResponse parent = SearchCriteriaConsultationResponse.builder()
                .identifier(10)
                .serviceId(705)
                .codeExterno(1)
                .dataTypeCode(2)
                .isMandatory("N")
                .labelCriteria("PADRE")
                .minimumLength(1)
                .maximumLength(10)
                .searchMethod(0)
                .abbreviation(null)
                .description("D")
                .codAction(1)
                .alias("a")
                .ordinal(1)
                .criterionCodeChild(999)
                .serviceName("SVC")
                .build();
        when(searchCriteriaProvider.getCriteria(705)).thenReturn(List.of(parent));

        SearchCriteriaRequest req = SearchCriteriaRequest.builder()
                .data(SearchCriteriaRequestData.builder().serviceId(705).build())
                .build();

        SearchCriteriaResponse out = service.obtainSearchCriteria(req);
        assertThat(out.getCriteria().get(0).getFields()).hasSize(1);
    }

    @Test
    void obtainSearchCriteria_buildsHierarchy() {
        SearchCriteriaConsultationResponse parent = SearchCriteriaConsultationResponse.builder()
                .identifier(10)
                .serviceId(705)
                .codeExterno(1)
                .dataTypeCode(2)
                .isMandatory("S")
                .labelCriteria("PADRE")
                .minimumLength(1)
                .maximumLength(10)
                .searchMethod(0)
                .abbreviation("A")
                .description("DESC PADRE")
                .codAction(1)
                .alias("aliasP")
                .ordinal(1)
                .criterionCodeChild(20)
                .serviceName("SVC")
                .build();
        SearchCriteriaConsultationResponse child = SearchCriteriaConsultationResponse.builder()
                .identifier(20)
                .serviceId(705)
                .codeExterno(2)
                .dataTypeCode(2)
                .isMandatory("N")
                .labelCriteria("HIJO")
                .minimumLength(0)
                .maximumLength(5)
                .searchMethod(0)
                .abbreviation("A")
                .description("DESC HIJO")
                .codAction(2)
                .alias("aliasH")
                .ordinal(2)
                .serviceName("SVC")
                .build();

        when(searchCriteriaProvider.getCriteria(705)).thenReturn(List.of(parent, child));

        SearchCriteriaRequest req = SearchCriteriaRequest.builder()
                .data(SearchCriteriaRequestData.builder().serviceId(705).type("D").build())
                .build();

        SearchCriteriaResponse out = service.obtainSearchCriteria(req);
        assertThat(out.getServiceId()).isEqualTo(705);
        assertThat(out.getCriteria()).hasSize(1);
        assertThat(out.getCriteria().get(0).getFields()).hasSize(2);
    }

    @Test
    void obtainSearchCriteria_ignoresDuplicateAbbreviationRows() {
        SearchCriteriaConsultationResponse a = SearchCriteriaConsultationResponse.builder()
                .identifier(1)
                .serviceId(705)
                .codeExterno(1)
                .dataTypeCode(2)
                .isMandatory("S")
                .labelCriteria("L1")
                .minimumLength(1)
                .maximumLength(5)
                .searchMethod(0)
                .abbreviation("X")
                .description("D1")
                .codAction(1)
                .alias("a1")
                .ordinal(1)
                .criterionCodeChild(null)
                .serviceName("S")
                .build();
        SearchCriteriaConsultationResponse b = SearchCriteriaConsultationResponse.builder()
                .identifier(2)
                .serviceId(705)
                .codeExterno(2)
                .dataTypeCode(2)
                .isMandatory("N")
                .labelCriteria("L2")
                .minimumLength(1)
                .maximumLength(5)
                .searchMethod(0)
                .abbreviation("X")
                .description("D2")
                .codAction(1)
                .alias("a2")
                .ordinal(2)
                .criterionCodeChild(null)
                .serviceName("S")
                .build();
        when(searchCriteriaProvider.getCriteria(705)).thenReturn(List.of(a, b));

        SearchCriteriaRequest req = SearchCriteriaRequest.builder()
                .data(SearchCriteriaRequestData.builder().serviceId(705).build())
                .build();

        SearchCriteriaResponse out = service.obtainSearchCriteria(req);
        assertThat(out.getCriteria()).hasSize(1);
    }
}
