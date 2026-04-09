package bo.com.bg.app.response;

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
public class SaguapacTokenResponse {

    @JsonProperty("token")
    private String token;

    @JsonProperty("valido")
    private boolean valido;

    @JsonProperty("mensaje")
    private String mensaje;
}
