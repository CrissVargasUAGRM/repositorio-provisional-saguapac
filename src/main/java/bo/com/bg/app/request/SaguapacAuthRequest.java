package bo.com.bg.app.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
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
public class SaguapacAuthRequest {

    @NotNull
    @NotBlank
    @JsonProperty("login")
    private String login;

    @NotNull
    @NotBlank
    @JsonProperty("password")
    private String password;

    @NotNull
    @NotBlank
    @JsonProperty("sucursal")
    private String sucursal;

    @NotNull
    @NotBlank
    @JsonProperty("cajero")
    private String cajero;

    @NotNull
    @NotBlank
    @JsonProperty("passwordCajero")
    private String passwordCajero;
}
