package bo.com.bg.app.rest.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import bo.com.bg.app.handler.SaguapacRestExceptionHandler;
import bo.com.bg.app.rest.request.ClientRequest;
import bo.com.bg.app.rest.response.ClientClienteGroup;
import bo.com.bg.app.rest.response.ClientResponseClient;
import bo.com.bg.app.rest.response.ClientResponseData;
import bo.com.bg.domain.service.ClientsService;
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
class ClientControllerTest {

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private ClientsService clientsService;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();
        mockMvc = MockMvcBuilders.standaloneSetup(new ClientController(clientsService))
                .setValidator(validator)
                .setControllerAdvice(new SaguapacRestExceptionHandler())
                .build();
    }

    @Test
    void consultar_returnsOk_withLibraryResponseShape() throws Exception {
        ClientResponseData data = ClientResponseData.builder()
                .serviceId(704)
                .clientes(List.of(ClientClienteGroup.builder()
                        .cliente(List.of(ClientResponseClient.builder()
                                .label("cliente")
                                .value("Juan Perez")
                                .mandatory(false)
                                .editable("N")
                                .description("cliente")
                                .code("cliente")
                                .build()))
                        .build()))
                .build();
        when(clientsService.getClient(any(ClientRequest.class))).thenReturn(data);

        mockMvc.perform(post("/v1/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.serviceId").value(704))
                .andExpect(jsonPath("$.data.clientes[0].cliente[0].code").value("cliente"));
    }

    private static ClientRequest validRequest() {
        return ClientRequest.builder()
                .data(ClientRequest.ClientRequestData.builder()
                        .serviceId(704)
                        .filtro(List.of(ClientRequest.FiltroItem.builder()
                                .identificador(3)
                                .alias("codigoSocio")
                                .valor("1")
                                .build()))
                        .build())
                .metadata(ClientRequest.ClientRequestMetadata.builder()
                        .codUsuario("TOP1")
                        .codSucursal(70)
                        .codAplicacion(1)
                        .build())
                .build();
    }
}
