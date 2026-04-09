package bo.com.bg.app.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Campo etiquetado de deuda para UI")
public class DebtResponseField {

    @JsonProperty("label")
    private String label;

    @JsonProperty("value")
    private String value;

    @JsonProperty("mandatory")
    private Boolean mandatory;

    @JsonProperty("editable")
    private String editable;

    @JsonProperty("description")
    private String description;

    @JsonProperty("code")
    private String code;

    @JsonProperty("grupo")
    private String grupo;

    @JsonProperty("indice")
    private String indice;

    @JsonProperty("tipoDato")
    private String tipoDato;
}
