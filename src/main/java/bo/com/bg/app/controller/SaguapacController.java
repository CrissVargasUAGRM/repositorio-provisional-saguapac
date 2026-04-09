package bo.com.bg.app.controller;

import bo.com.bg.app.request.SaguapacAuthRequest;
import bo.com.bg.app.response.SaguapacTokenResponse;
import bo.com.bg.commons.dto.Response;
import bo.com.bg.domain.service.SaguapacAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/saguapac")
@AllArgsConstructor
@Tag(name = "Saguapac", description = "Servicios de autenticacion para Saguapac")
public class SaguapacController extends AbstractController {

    private final SaguapacAuthService saguapacAuthService;

    @PostMapping("/auth")
    @Operation(summary = "Autenticacion con Saguapac")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticacion exitosa"),
            @ApiResponse(responseCode = "400", description = "Request invalido"),
            @ApiResponse(responseCode = "401", description = "Credenciales incorrectas"),
            @ApiResponse(responseCode = "403", description = "Sin autorizacion"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
    public ResponseEntity<Response<SaguapacTokenResponse>> authenticate(@Valid @RequestBody SaguapacAuthRequest request) {
        logger().debug("POST /v1/saguapac/auth");
        SaguapacTokenResponse body = saguapacAuthService.authenticate(request);
        if (!body.isValido()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Response.<SaguapacTokenResponse>builder()
                            .success(false)
                            .message(body.getMensaje())
                            .data(body)
                            .build());
        }
        return ResponseEntity.ok(Response.<SaguapacTokenResponse>builder()
                .success(true)
                .data(body)
                .message(body.getMensaje())
                .build());
    }
}
