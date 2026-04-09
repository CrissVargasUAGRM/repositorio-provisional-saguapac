package bo.com.bg.domain.provider;

import static org.assertj.core.api.Assertions.assertThat;

import bo.com.bg.domain.provider.impl.DefaultProvider;
import org.junit.jupiter.api.Test;

class DefaultProviderTest {

    @Test
    void readyByDefault() {
        assertThat(new DefaultProvider().ready()).isTrue();
    }
}
