package bo.com.bg.domain.request;

import static org.assertj.core.api.Assertions.assertThat;

import bo.com.bg.domain.connector.request.OperationContext;
import bo.com.bg.domain.service.request.ServiceCommand;
import org.junit.jupiter.api.Test;

class DomainDtoTest {

    @Test
    void operationContext() {
        assertThat(new OperationContext("x").operationId()).isEqualTo("x");
        assertThat(new OperationContext(null).operationId()).isEmpty();
    }

    @Test
    void serviceCommandDefault() {
        assertThat(new ServiceCommand(null).action()).isEqualTo("PING");
    }
}
