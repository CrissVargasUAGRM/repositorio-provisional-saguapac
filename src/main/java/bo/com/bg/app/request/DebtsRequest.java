package bo.com.bg.app.request;

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
@Schema(description = "Consulta de deudas por criterios (Síntesis)")
public class DebtsRequest {

    @Valid
    @NotNull
    @JsonProperty("data")
    private DebtsRequestData data;

    @Valid
    @NotNull
    @JsonProperty("metadata")
    private DebtsRequestMetadata metadata;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DebtsRequestData {

        @NotNull
        @JsonProperty("serviceId")
        private Integer serviceId;

        @NotNull
        @NotEmpty
        @JsonProperty("filtro")
        private List<DebtsFiltroItem> filtro;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DebtsFiltroItem {

        @NotNull
        @JsonProperty("label")
        private String label;

        @NotNull
        @JsonProperty("value")
        private String value;

        @JsonProperty("mandatory")
        private Boolean mandatory;

        @JsonProperty("editable")
        private String editable;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DebtsRequestMetadata {

        @JsonProperty("codUsuario")
        private String codUsuario;

        @JsonProperty("codSucursal")
        private Integer codSucursal;

        @JsonProperty("codAplicacion")
        private Integer codAplicacion;
    }
}
