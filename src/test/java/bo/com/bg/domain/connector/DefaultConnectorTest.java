package bo.com.bg.domain.connector;

import static org.assertj.core.api.Assertions.assertThat;

import bo.com.bg.domain.connector.impl.DefaultConnector;
import bo.com.bg.domain.connector.request.OperationContext;
import org.junit.jupiter.api.Test;

class DefaultConnectorTest {

    private final DefaultConnector connector = new DefaultConnector();

    @Test
    void resourceName() {
        assertThat(connector.resourceName()).isEqualTo("default-connector");
    }

    @Test
    void executeAcceptsNonBlank() {
        assertThat(connector.execute(new OperationContext("op")).accepted()).isTrue();
    }

    @Test
    void executeRejectsBlank() {
        assertThat(connector.execute(new OperationContext("")).accepted()).isFalse();
    }

    @Test
    void executeRejectsNullContext() {
        assertThat(connector.execute(null).accepted()).isFalse();
    }
}
