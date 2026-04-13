package bo.com.bg.app.response;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class SearchCriteriaResponseCriteria {

    @JsonProperty("grupo")
    private Integer group;

    @JsonProperty("descripcion")
    private String description;

    @JsonProperty("campos")
    private List<SearchCriteriaResponseCriteriaItem> fields;
}
