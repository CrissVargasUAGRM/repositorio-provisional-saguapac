package bo.com.bg.app.request;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PingRequestTest {

    @Test
    void normalizesNullTraceId() {
        assertThat(new PingRequest(null).traceId()).isEmpty();
    }

    @Test
    void keepsValue() {
        assertThat(new PingRequest("t-1").traceId()).isEqualTo("t-1");
    }
}
