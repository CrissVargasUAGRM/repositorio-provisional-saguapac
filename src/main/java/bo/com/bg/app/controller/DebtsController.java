package bo.com.bg.app.controller;

import bo.com.bg.app.request.DebtsRequest;
import bo.com.bg.app.response.DebtsResponse;
import bo.com.bg.commons.models.ErrorResponse;
import bo.com.bg.commons.models.Response;
import bo.com.bg.domain.service.DebtsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/debts")
@AllArgsConstructor
@Tag(name = "Deudas", description = "Consulta de deudas / facturas pendientes vía Saguapac (Síntesis)")
public class DebtsController extends AbstractController {

    private final DebtsService debtsService;

    @PostMapping
    @Operation(summary = "Consultar deudas por código de conexión (Síntesis → Saguapac)")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "OK"),
                @ApiResponse(
                        responseCode = "400",
                        description = "Request inválido",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                @ApiResponse(
                        responseCode = "406",
                        description = "Datos no aceptables",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                @ApiResponse(responseCode = "500", description = "Error interno")
            })
    public Response<DebtsResponse> consultar(@Valid @RequestBody DebtsRequest request) {
        logger().debug("POST /v1/debts");
        return Response.of(debtsService.getDebts(request));
    }
}
