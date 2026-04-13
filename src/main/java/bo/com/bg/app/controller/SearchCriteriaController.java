package bo.com.bg.app.controller;

import bo.com.bg.app.request.SearchCriteriaRequest;
import bo.com.bg.app.response.SearchCriteriaResponse;
import bo.com.bg.commons.models.ErrorResponse;
import bo.com.bg.commons.models.Response;
import bo.com.bg.domain.service.CriteriaSearchService;
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
@RequestMapping("/v1/searchCriteria")
@AllArgsConstructor
@Tag(name = "Criterios de busqueda", description = "Servicios para obtener los criterios de busquedas de los endpoints")
public class SearchCriteriaController extends AbstractController {

    private final CriteriaSearchService service;

    @PostMapping
    @Operation(summary = "Obtener criterios de busqueda")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Criterios de búsqueda",
                        content = @Content(schema = @Schema(implementation = SearchCriteriaResponse.class))),
                @ApiResponse(
                        responseCode = "400",
                        description = "Data Validation Error.",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                @ApiResponse(
                        responseCode = "401",
                        description = "Access Denied.",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                @ApiResponse(
                        responseCode = "403",
                        description = "Forbidden error",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                @ApiResponse(
                        responseCode = "404",
                        description = "Not found",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                @ApiResponse(
                        responseCode = "405",
                        description = "Method not allowed",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                @ApiResponse(
                        responseCode = "406",
                        description = "Not acceptable",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                @ApiResponse(
                        responseCode = "500",
                        description = "Internal error",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    public Response<SearchCriteriaResponse> obtainSearchCriteria(@Valid @RequestBody SearchCriteriaRequest request) {
        logger().debug("POST /v1/searchCriteria");
        return Response.of(service.obtainSearchCriteria(request));
    }
}
