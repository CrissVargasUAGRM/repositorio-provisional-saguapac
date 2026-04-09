package bo.com.bg.domain.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import bo.com.bg.app.rest.request.ClientRequest;
import bo.com.bg.app.rest.response.ClientResponseData;
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
class ClientsServiceImplTest {

    @Mock
    private InvoicePendingConnector invoicePendingConnector;

    @Mock
    private ProviderSaguapacConfig config;

    @InjectMocks
    private ClientsServiceImpl clientsService;

    @Test
    void getClient_mockEnabled_usesMockWithoutCallingConnector() {
        when(config.isMockEnabled()).thenReturn(true);

        ClientResponseData r = clientsService.getClient(sampleRequest());

        assertThat(r.getServiceId()).isEqualTo(704);
        assertThat(r.getClientes()).isNotEmpty();
        verify(invoicePendingConnector, never()).getPendingInvoices(anyString());
    }

    @Test
    void getClient_real_callsConnectorAndMaps() {
        when(config.isMockEnabled()).thenReturn(false);
        when(invoicePendingConnector.getPendingInvoices("99")).thenReturn(
                InvoicePendingResponseDTO.builder()
                        .partner(InvoicePendingResponseDTO.PartnerDto.builder()
                                .connectionId("1")
                                .connectionCode("2")
                                .firstName("A")
                                .lastName("B")
                                .address("dir")
                                .lastPaidInvoiceDate("01/01/26")
                                .documentNumber("123")
                                .email("e@e.com")
                                .mobile("7")
                                .build())
                        .build());

        ClientResponseData r = clientsService.getClient(sampleRequestWithCodigo("99"));

        assertThat(r.getServiceId()).isEqualTo(704);
        assertThat(r.getClientes().get(0).getCliente()).isNotEmpty();
        assertThat(r.getClientes().get(0).getCliente().stream()
                        .anyMatch(c -> "idConexion".equals(c.getCode())))
                .isTrue();
    }

    @Test
    void getClient_nullPayload_throws() {
        assertThatThrownBy(() -> clientsService.getClient(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("406");
    }

    @Test
    void getClient_missingCodigoSocio_throws() {
        ClientRequest req = ClientRequest.builder()
                .data(ClientRequest.ClientRequestData.builder()
                        .serviceId(1)
                        .filtro(List.of(ClientRequest.FiltroItem.builder()
                                .alias("otro")
                                .valor("x")
                                .build()))
                        .build())
                .metadata(ClientRequest.ClientRequestMetadata.builder().build())
                .build();

        assertThatThrownBy(() -> clientsService.getClient(req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("codigoSocio");
    }

    @Test
    void getClient_providerException_wraps() {
        when(config.isMockEnabled()).thenReturn(false);
        when(invoicePendingConnector.getPendingInvoices(anyString()))
                .thenThrow(new ProviderException("fallo", "X", HttpStatus.BAD_REQUEST));

        assertThatThrownBy(() -> clientsService.getClient(sampleRequest()))
                .isInstanceOf(ProviderException.class)
                .hasMessageContaining("servicio externo");
    }

    private static ClientRequest sampleRequest() {
        return sampleRequestWithCodigo("1");
    }

    private static ClientRequest sampleRequestWithCodigo(String codigo) {
        return ClientRequest.builder()
                .data(ClientRequest.ClientRequestData.builder()
                        .serviceId(704)
                        .filtro(List.of(ClientRequest.FiltroItem.builder()
                                .alias("codigoSocio")
                                .valor(codigo)
                                .build()))
                        .build())
                .metadata(ClientRequest.ClientRequestMetadata.builder().build())
                .build();
    }
}
