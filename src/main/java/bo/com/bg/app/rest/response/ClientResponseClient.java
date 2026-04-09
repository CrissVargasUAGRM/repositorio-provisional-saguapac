package bo.com.bg.app.rest.response;

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
@Schema(description = "Campo etiquetado para UI")
public class ClientResponseClient {

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
}
