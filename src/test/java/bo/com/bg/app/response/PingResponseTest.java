package bo.com.bg.app.response;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PingResponseTest {

    @Test
    void normalizesNulls() {
        PingResponse r = new PingResponse(null, null);
        assertThat(r.status()).isEqualTo("UNKNOWN");
        assertThat(r.detail()).isEmpty();
    }
}
