package bo.com.bg.domain.provider.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import bo.com.bg.commons.db.QueryManager;
import bo.com.bg.commons.enums.ErrorMessage;
import bo.com.bg.commons.exceptions.ProviderException;
import bo.com.bg.domain.config.ProviderConfigSql;
import bo.com.bg.domain.provider.response.SearchCriteriaConsultationResponse;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class SearchCriteriaProviderImplTest {

    @Mock
    private QueryManager<?> queryManager;

    @Mock
    private ProviderConfigSql config;

    private SearchCriteriaProviderImpl provider;

    @BeforeEach
    void setUp() {
        provider = new SearchCriteriaProviderImpl(queryManager, config);
    }

    @Test
    void getCriteria_throws406_whenQueryNull() {
        when(config.getQueryGetCriteria()).thenReturn(null);
        assertThatThrownBy(() -> provider.getCriteria(705))
                .isInstanceOf(ProviderException.class)
                .satisfies(ex -> assertThat(((ProviderException) ex).getStatus()).isEqualTo(HttpStatus.NOT_ACCEPTABLE));
    }

    @Test
    void getCriteria_throws406_whenQueryBlank() {
        when(config.getQueryGetCriteria()).thenReturn("  ");
        assertThatThrownBy(() -> provider.getCriteria(705))
                .isInstanceOf(ProviderException.class)
                .satisfies(ex -> {
                    ProviderException pe = (ProviderException) ex;
                    assertThat(pe.getStatus()).isEqualTo(HttpStatus.NOT_ACCEPTABLE);
                    assertThat(pe.getCodeError()).isEqualTo(ErrorMessage.CRITERIA_SEARCH_ERROR.getCod());
                });
    }

    @Test
    void getCriteria_throws406_whenEmptyList() {
        when(config.getQueryGetCriteria()).thenReturn("SELECT 1");
        when(queryManager.executeQueryForList(eq("SELECT 1"), any(), eq(SearchCriteriaConsultationResponse.class)))
                .thenReturn(List.of());
        assertThatThrownBy(() -> provider.getCriteria(705))
                .isInstanceOf(ProviderException.class)
                .satisfies(ex -> assertThat(((ProviderException) ex).getStatus()).isEqualTo(HttpStatus.NOT_ACCEPTABLE));
    }

    @Test
    void getCriteria_returnsList() {
        when(config.getQueryGetCriteria()).thenReturn("SELECT 1");
        List<SearchCriteriaConsultationResponse> rows = List.of(
                SearchCriteriaConsultationResponse.builder().identifier(1).build());
        when(queryManager.executeQueryForList(eq("SELECT 1"), any(), eq(SearchCriteriaConsultationResponse.class)))
                .thenReturn(rows);
        assertThat(provider.getCriteria(705)).hasSize(1);
    }

    @Test
    void getCriteria_throws406_whenNullList() {
        when(config.getQueryGetCriteria()).thenReturn("SELECT 1");
        when(queryManager.executeQueryForList(eq("SELECT 1"), any(), eq(SearchCriteriaConsultationResponse.class)))
                .thenReturn(null);
        assertThatThrownBy(() -> provider.getCriteria(705))
                .isInstanceOf(ProviderException.class)
                .satisfies(ex -> assertThat(((ProviderException) ex).getStatus()).isEqualTo(HttpStatus.NOT_ACCEPTABLE));
    }
}
