package bo.com.bg.domain.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import bo.com.bg.app.request.DebtsRequest;
import bo.com.bg.app.response.DebtsResponse;
import bo.com.bg.commons.exceptions.ProviderException;
import bo.com.bg.domain.config.ProviderSaguapacConfig;
import bo.com.bg.domain.connector.InvoicePendingConnector;
import bo.com.bg.domain.connector.response.InvoicePendingResponseDTO;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class DebtsServiceImplTest {

    @Mock
    private InvoicePendingConnector invoicePendingConnector;

    @Mock
    private ProviderSaguapacConfig config;

    @InjectMocks
    private DebtsServiceImpl debtsService;

    @Test
    void getDebts_mockEnabled_usesMockWithoutCallingConnector() {
        when(config.isMockEnabled()).thenReturn(true);

        DebtsResponse r = debtsService.getDebts(sampleRequest());

        assertThat(r.getServiceId()).isEqualTo(704);
        assertThat(r.getDeudas()).isNotEmpty();
        verify(invoicePendingConnector, never()).getPendingInvoices(anyString());
    }

    @Test
    void getDebts_real_callsConnectorAndMapsPeriodParts() {
        when(config.isMockEnabled()).thenReturn(false);
        when(invoicePendingConnector.getPendingInvoices("970773800"))
                .thenReturn(
                        InvoicePendingResponseDTO.builder()
                                .partner(InvoicePendingResponseDTO.PartnerDto.builder()
                                        .connectionId("115000")
                                        .connectionCode("970773800")
                                        .electronicInvoices(List.of(
                                                InvoicePendingResponseDTO.ElectronicInvoiceDto.builder()
                                                        .invoiceId("4559847148")
                                                        .period("2020-10")
                                                        .totalLabel("Bs. 34.32")
                                                        .totalAmount(34.32)
                                                        .blockedByQr("NO")
                                                        .build()))
                                        .build())
                                .build());

        DebtsResponse r = debtsService.getDebts(sampleRequestWithCodigo("970773800"));

        assertThat(r.getServiceId()).isEqualTo(704);
        assertThat(r.getDeudas()).hasSize(1);
        assertThat(r.getDeudas().get(0).getDeuda()).isNotEmpty();
        assertThat(r.getDeudas().get(0).getDeuda().stream()
                        .filter(f -> "periodo".equals(f.getCode()))
                        .map(f -> f.getValue())
                        .findFirst())
                .contains("2020");
        assertThat(r.getDeudas().get(0).getDeuda().stream()
                        .filter(f -> "mesPeriodo".equals(f.getCode()))
                        .map(f -> f.getValue())
                        .findFirst())
                .contains("10");
        assertThat(r.getDeudas().get(0).getDeuda().stream()
                        .allMatch(f -> f.getIndice() != null && !f.getIndice().isBlank()))
                .isTrue();
    }

    @Test
    void getDebts_nullPayload_throws() {
        assertThatThrownBy(() -> debtsService.getDebts(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("406");
    }

    @Test
    void getDebts_missingCodigoConexion_throws() {
        DebtsRequest req = DebtsRequest.builder()
                .data(DebtsRequest.DebtsRequestData.builder()
                        .serviceId(1)
                        .filtro(List.of(DebtsRequest.DebtsFiltroItem.builder()
                                .label("otro")
                                .value("x")
                                .build()))
                        .build())
                .metadata(DebtsRequest.DebtsRequestMetadata.builder().build())
                .build();

        assertThatThrownBy(() -> debtsService.getDebts(req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("codigoConexion");
    }

    @Test
    void getDebts_providerException_wraps() {
        when(config.isMockEnabled()).thenReturn(false);
        when(invoicePendingConnector.getPendingInvoices(anyString()))
                .thenThrow(new ProviderException("fallo", "X", HttpStatus.BAD_REQUEST));

        assertThatThrownBy(() -> debtsService.getDebts(sampleRequest()))
                .isInstanceOf(ProviderException.class)
                .hasMessageContaining("servicio externo");
    }

    private static DebtsRequest sampleRequest() {
        return sampleRequestWithCodigo("1");
    }

    private static DebtsRequest sampleRequestWithCodigo(String codigo) {
        return DebtsRequest.builder()
                .data(DebtsRequest.DebtsRequestData.builder()
                        .serviceId(704)
                        .filtro(List.of(DebtsRequest.DebtsFiltroItem.builder()
                                .label("codigoConexion")
                                .value(codigo)
                                .mandatory(true)
                                .editable("S")
                                .build()))
                        .build())
                .metadata(DebtsRequest.DebtsRequestMetadata.builder()
                        .codUsuario("JBK")
                        .codSucursal(701)
                        .codAplicacion(5)
                        .build())
                .build();
    }
}
