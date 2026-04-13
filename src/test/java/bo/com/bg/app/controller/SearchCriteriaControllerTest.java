package bo.com.bg.app.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import bo.com.bg.app.handler.SaguapacRestExceptionHandler;
import bo.com.bg.app.request.SearchCriteriaRequest;
import bo.com.bg.app.request.SearchCriteriaRequestData;
import bo.com.bg.app.response.SearchCriteriaResponse;
import bo.com.bg.app.response.SearchCriteriaResponseCriteria;
import bo.com.bg.app.response.SearchCriteriaResponseCriteriaItem;
import bo.com.bg.domain.service.CriteriaSearchService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@ExtendWith(MockitoExtension.class)
class SearchCriteriaControllerTest {

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private CriteriaSearchService criteriaSearchService;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();
        mockMvc = MockMvcBuilders.standaloneSetup(new SearchCriteriaController(criteriaSearchService))
                .setValidator(validator)
                .setControllerAdvice(new SaguapacRestExceptionHandler())
                .build();
    }

    @Test
    void obtainSearchCriteria_returnsOk() throws Exception {
        SearchCriteriaResponse data = SearchCriteriaResponse.builder()
                .serviceId(705)
                .criteria(List.of(SearchCriteriaResponseCriteria.builder()
                        .group(1)
                        .description("d")
                        .fields(List.of(SearchCriteriaResponseCriteriaItem.builder()
                                .label("l")
                                .type(2)
                                .required(true)
                                .identifier(1)
                                .service("S")
                                .serviceType(null)
                                .operationCode(null)
                                .group(1)
                                .order(1)
                                .value("0")
                                .abbreviation("1")
                                .description("x")
                                .size(15)
                                .actionCode(10)
                                .alias("a")
                                .visible(false)
                                .build()))
                        .build()))
                .build();
        when(criteriaSearchService.obtainSearchCriteria(any(SearchCriteriaRequest.class))).thenReturn(data);

        mockMvc.perform(post("/v1/searchCriteria")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.serviceId").value(705))
                .andExpect(jsonPath("$.data.criterios[0].campos[0].etiqueta").value("l"));
    }

    private static SearchCriteriaRequest validRequest() {
        return SearchCriteriaRequest.builder()
                .data(SearchCriteriaRequestData.builder().serviceId(705).type("D").build())
                .build();
    }
}
