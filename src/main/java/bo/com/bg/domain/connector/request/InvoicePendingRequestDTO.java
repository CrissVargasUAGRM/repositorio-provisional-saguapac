package bo.com.bg.domain.connector.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Cuerpo opcional hacia Saguapac; el contrato real usa {@code codigoSocio} como query param en GET.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoicePendingRequestDTO {

    @JsonProperty("codigoSocio")
    private String partnerCode;
}
