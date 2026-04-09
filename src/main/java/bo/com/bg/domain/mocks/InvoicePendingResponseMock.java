package bo.com.bg.domain.mocks;

import bo.com.bg.domain.connector.response.InvoicePendingResponseDTO;
import java.util.List;

public final class InvoicePendingResponseMock {

    private InvoicePendingResponseMock() {}

    public static InvoicePendingResponseDTO sample() {
        return InvoicePendingResponseDTO.builder()
                .partner(InvoicePendingResponseDTO.PartnerDto.builder()
                        .connectionId("62889")
                        .connectionCode("050801100")
                        .firstName("JOSE IGNACIO")
                        .lastName("SANDY SILES")
                        .address("C/ ELDA VIERA N° . UV 66 MZ 9 B. UNION")
                        .lastPaidInvoiceDate("30/03/26")
                        .documentNumber("1058527")
                        .email("")
                        .mobile("")
                        .qrDurationMinutes(5)
                        .electronicInvoices(List.of(InvoicePendingResponseDTO.ElectronicInvoiceDto.builder()
                                .invoiceId("3126523284")
                                .period("2022-09")
                                .totalLabel("Bs. 20.50")
                                .totalAmount(20.5)
                                .blockedByQr("NO")
                                .detailLines(List.of(InvoicePendingResponseDTO.DetailLineDto.builder()
                                        .item("CARGO MINIMO")
                                        .amountLabel("Bs. 20.00")
                                        .build()))
                                .build()))
                        .build())
                .build();
    }
}
