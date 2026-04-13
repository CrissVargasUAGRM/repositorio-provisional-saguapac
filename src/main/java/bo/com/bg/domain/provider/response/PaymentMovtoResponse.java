package bo.com.bg.domain.provider.response;

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
public class PaymentMovtoResponse {

    private String pStrCodError;
    private String pStrDescError;
    private Long pIntIdentificador;
}
