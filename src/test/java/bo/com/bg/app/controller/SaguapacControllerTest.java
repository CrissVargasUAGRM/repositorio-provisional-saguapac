package bo.com.bg.app.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import bo.com.bg.app.handler.SaguapacRestExceptionHandler;
import bo.com.bg.app.request.SaguapacAuthRequest;
import bo.com.bg.app.response.SaguapacTokenResponse;
import bo.com.bg.domain.service.SaguapacAuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
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
class SaguapacControllerTest {

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private SaguapacAuthService saguapacAuthService;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();
        mockMvc = MockMvcBuilders.standaloneSetup(new SaguapacController(saguapacAuthService))
                .setValidator(validator)
                .setControllerAdvice(new SaguapacRestExceptionHandler())
                .build();
    }

    @Test
    void authSuccess() throws Exception {
        when(saguapacAuthService.authenticate(any(SaguapacAuthRequest.class))).thenReturn(
                SaguapacTokenResponse.builder().token("t").valido(true).mensaje(null).build());

        mockMvc.perform(post("/v1/saguapac/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validBody())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.valido").value(true))
                .andExpect(jsonPath("$.data.token").value("t"));
    }

    @Test
    void authFailsReturns401() throws Exception {
        when(saguapacAuthService.authenticate(any(SaguapacAuthRequest.class))).thenReturn(
                SaguapacTokenResponse.builder().valido(false).mensaje("error").build());

        mockMvc.perform(post("/v1/saguapac/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validBody())))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void authValidationError() throws Exception {
        Map<String, String> body = Map.of(
                "login", "",
                "password", "p",
                "sucursal", "s",
                "cajero", "c",
                "passwordCajero", "x");

        mockMvc.perform(post("/v1/saguapac/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }

    private static Map<String, String> validBody() {
        return Map.of(
                "login", "l",
                "password", "p",
                "sucursal", "s",
                "cajero", "c",
                "passwordCajero", "pc");
    }
}
