package bo.com.bg.app.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import bo.com.bg.app.handler.SaguapacRestExceptionHandler;
import bo.com.bg.app.request.DebtsRequest;
import bo.com.bg.app.response.DebtResponseField;
import bo.com.bg.app.response.DebtsDeudaGroup;
import bo.com.bg.app.response.DebtsResponse;
import bo.com.bg.domain.service.DebtsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.UUID;
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
class DebtsControllerTest {

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private DebtsService debtsService;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();
        mockMvc = MockMvcBuilders.standaloneSetup(new DebtsController(debtsService))
                .setValidator(validator)
                .setControllerAdvice(new SaguapacRestExceptionHandler())
                .build();
    }

    @Test
    void consultar_returnsOk_withLibraryResponseShape() throws Exception {
        String uuid = UUID.randomUUID().toString();
        DebtsResponse data = DebtsResponse.builder()
                .serviceId(704)
                .deudas(List.of(DebtsDeudaGroup.builder()
                        .deuda(List.of(DebtResponseField.builder()
                                .label("gestion")
                                .value("2018")
                                .mandatory(false)
                                .editable("N")
                                .description(null)
                                .code("gestion")
                                .grupo("deuda")
                                .indice(uuid)
                                .tipoDato("N")
                                .build()))
                        .build()))
                .build();
        when(debtsService.getDebts(any(DebtsRequest.class))).thenReturn(data);

        mockMvc.perform(post("/v1/debts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.serviceId").value(704))
                .andExpect(jsonPath("$.data.deudas[0].deuda[0].code").value("gestion"));
    }

    private static DebtsRequest validRequest() {
        return DebtsRequest.builder()
                .data(DebtsRequest.DebtsRequestData.builder()
                        .serviceId(704)
                        .filtro(List.of(DebtsRequest.DebtsFiltroItem.builder()
                                .label("codigoConexion")
                                .value("1")
                                .mandatory(true)
                                .editable("S")
                                .build()))
                        .build())
                .metadata(DebtsRequest.DebtsRequestMetadata.builder()
                        .codUsuario("JBK")
                        .codSucursal(701)
                        .codAplicacion(5)
                        .build())
                .build();
    }
}
