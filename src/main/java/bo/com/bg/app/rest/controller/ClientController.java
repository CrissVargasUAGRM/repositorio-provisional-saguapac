package bo.com.bg.app.rest.controller;

import bo.com.bg.app.controller.AbstractController;
import bo.com.bg.app.rest.request.ClientRequest;
import bo.com.bg.app.rest.response.ClientResponseData;
import bo.com.bg.commons.models.ErrorResponse;
import bo.com.bg.commons.models.Response;
import bo.com.bg.domain.service.ClientsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/clients")
@AllArgsConstructor
@Tag(name = "Clientes", description = "Consulta de socios/clientes vía Saguapac (Síntesis)")
public class ClientController extends AbstractController {

    private final ClientsService clientsService;

    @PostMapping
    @Operation(summary = "Consultar cliente por criterios (Síntesis → Saguapac)")
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
    public ResponseEntity<Response<ClientResponseData>> consultar(@Valid @RequestBody ClientRequest request) {
        logger().debug("POST /v1/clients");
        return ResponseEntity.ok(Response.of(clientsService.getClient(request)));
    }
}
