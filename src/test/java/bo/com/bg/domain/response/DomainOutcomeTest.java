package bo.com.bg.domain.response;

import static org.assertj.core.api.Assertions.assertThat;

import bo.com.bg.domain.connector.response.OperationResult;
import bo.com.bg.domain.service.response.ServiceOutcome;
import org.junit.jupiter.api.Test;

class DomainOutcomeTest {

    @Test
    void operationResult() {
        assertThat(new OperationResult(true).accepted()).isTrue();
    }

    @Test
    void serviceOutcome() {
        assertThat(new ServiceOutcome(null).code()).isEqualTo("OK");
        assertThat(new ServiceOutcome("ERR").code()).isEqualTo("ERR");
    }
}
