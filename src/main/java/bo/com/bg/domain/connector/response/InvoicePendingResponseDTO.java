package bo.com.bg.domain.connector.response;

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
public class InvoicePendingResponseDTO {

    @JsonProperty("socio")
    private PartnerDto partner;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PartnerDto {

        @JsonProperty("idConexion")
        private String connectionId;

        @JsonProperty("codigoConexion")
        private String connectionCode;

        @JsonProperty("nombre")
        private String firstName;

        @JsonProperty("apellidos")
        private String lastName;

        @JsonProperty("direccion")
        private String address;

        @JsonProperty("ultimaFechaFacturaPagada")
        private String lastPaidInvoiceDate;

        @JsonProperty("numeroDocumento")
        private String documentNumber;

        @JsonProperty("correoElectronico")
        private String email;

        @JsonProperty("celular")
        private String mobile;

        @JsonProperty("tiempoDuracionQR")
        private Integer qrDurationMinutes;

        @JsonProperty("facturasEL")
        private List<ElectronicInvoiceDto> electronicInvoices;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ElectronicInvoiceDto {

        @JsonProperty("idFactura")
        private String invoiceId;

        @JsonProperty("periodo")
        private String period;

        @JsonProperty("total")
        private String totalLabel;

        @JsonProperty("importeTotal")
        private Double totalAmount;

        @JsonProperty("bloqueadoPorQR")
        private String blockedByQr;

        @JsonProperty("detalle")
        private List<DetailLineDto> detailLines;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetailLineDto {

        @JsonProperty("item")
        private String item;

        @JsonProperty("monto")
        private String amountLabel;
    }
}
