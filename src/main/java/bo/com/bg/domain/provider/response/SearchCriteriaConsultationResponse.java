package bo.com.bg.domain.provider.response;

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
public class SearchCriteriaConsultationResponse {

    @JsonProperty("identifier")
    private Integer identifier;

    @JsonProperty("serviceId")
    private Integer serviceId;

    @JsonProperty("codeExterno")
    private Integer codeExterno;

    @JsonProperty("dataTypeCode")
    private Integer dataTypeCode;

    @JsonProperty("isMandatory")
    private String isMandatory;

    @JsonProperty("labelCriteria")
    private String labelCriteria;

    @JsonProperty("minimumLength")
    private Integer minimumLength;

    @JsonProperty("maximumLength")
    private Integer maximumLength;

    @JsonProperty("searchMethod")
    private Integer searchMethod;

    @JsonProperty("values")
    private String values;

    @JsonProperty("valueDefault")
    private String valueDefault;

    @JsonProperty("abbreviation")
    private String abbreviation;

    @JsonProperty("description")
    private String description;

    @JsonProperty("codAction")
    private Integer codAction;

    @JsonProperty("alias")
    private String alias;

    @JsonProperty("flowCodeHab")
    private Integer flowCodeHab;

    @JsonProperty("isSubservice")
    private String isSubservice;

    @JsonProperty("criterionCodeSearch")
    private Integer criterionCodeSearch;

    @JsonProperty("criterionCodeChild")
    private Integer criterionCodeChild;

    @JsonProperty("ordinal")
    private Integer ordinal;

    @JsonProperty("serviceName")
    private String serviceName;
}
