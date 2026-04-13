package bo.com.bg.app.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
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
public class SearchCriteriaRequestData {

    @NotNull
    @JsonProperty("serviceId")
    private Integer serviceId;

    @JsonProperty("type")
    private String type;
}
