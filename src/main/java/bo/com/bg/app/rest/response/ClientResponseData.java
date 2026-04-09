package bo.com.bg.app.rest.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Datos de clientes para Síntesis")
public class ClientResponseData {

    @JsonProperty("clientes")
    private List<ClientClienteGroup> clientes;

    @JsonProperty("serviceId")
    private Integer serviceId;
}
