package bo.com.bg.domain.connector.request;

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
public class SaguapacLoginRequest {

    @JsonProperty("login")
    private String login;

    @JsonProperty("pass")
    private String pass;

    @JsonProperty("sucursal")
    private String sucursal;

    @JsonProperty("cajero")
    private String cajero;

    @JsonProperty("passCajero")
    private String passCajero;
}
