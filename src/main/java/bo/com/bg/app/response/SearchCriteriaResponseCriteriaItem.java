package bo.com.bg.app.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class SearchCriteriaResponseCriteriaItem {

    @JsonProperty("etiqueta")
    private String label;

    @JsonProperty("tipo")
    private Integer type;

    @JsonProperty("requerido")
    private Boolean required;

    @JsonProperty("identificador")
    private Integer identifier;

    @JsonProperty("servicio")
    private String service;

    @JsonInclude(JsonInclude.Include.ALWAYS)
    @JsonProperty("tipo_servicio")
    private String serviceType;

    @JsonInclude(JsonInclude.Include.ALWAYS)
    @JsonProperty("codigo_operacion")
    private String operationCode;

    @JsonProperty("grupo")
    private Integer group;

    @JsonProperty("orden")
    private Integer order;

    @JsonProperty("valor")
    private String value;

    @JsonProperty("abreviatura")
    private String abbreviation;

    @JsonProperty("descripcion")
    private String description;

    @JsonProperty("tamanio")
    private Integer size;

    @JsonProperty("codigo_accion")
    private Integer actionCode;

    @JsonProperty("alias")
    private String alias;

    @JsonProperty("visible")
    private Boolean visible;
}
