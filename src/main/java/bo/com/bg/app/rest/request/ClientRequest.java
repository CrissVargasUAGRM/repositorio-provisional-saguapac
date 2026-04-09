package bo.com.bg.app.rest.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Consulta de cliente por criterios (Síntesis)")
public class ClientRequest {

    @Valid
    @NotNull
    @JsonProperty("data")
    private ClientRequestData data;

    @Valid
    @NotNull
    @JsonProperty("metadata")
    private ClientRequestMetadata metadata;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClientRequestData {

        @NotNull
        @JsonProperty("serviceId")
        private Integer serviceId;

        @NotNull
        @NotEmpty
        @JsonProperty("filtro")
        private List<FiltroItem> filtro;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FiltroItem {

        @JsonProperty("identificador")
        private Integer identificador;

        @NotNull
        @JsonProperty("alias")
        private String alias;

        @NotNull
        @JsonProperty("valor")
        private String valor;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClientRequestMetadata {

        @JsonProperty("codUsuario")
        private String codUsuario;

        @JsonProperty("codSucursal")
        private Integer codSucursal;

        @JsonProperty("codAplicacion")
        private Integer codAplicacion;
    }
}
