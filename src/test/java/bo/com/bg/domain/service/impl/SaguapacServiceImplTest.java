package bo.com.bg.domain.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import bo.com.bg.domain.connector.Connector;
import bo.com.bg.domain.provider.Provider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SaguapacServiceImplTest {

    @Mock
    private Connector connector;

    @Mock
    private Provider provider;

    @InjectMocks
    private SaguapacServiceImpl service;

    @Test
    void pingWhenReady() {
        when(provider.ready()).thenReturn(true);

        assertThat(service.ping().status()).isEqualTo("UP");
    }

    @Test
    void pingWhenNotReady() {
        when(provider.ready()).thenReturn(false);
        when(connector.resourceName()).thenReturn("c");

        var response = service.ping();
        assertThat(response.status()).isEqualTo("DOWN");
        assertThat(response.detail()).contains("c").contains("PING");
    }
}
