package bo.com.bg.domain.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class JsonPayloadUtilTest {

    @Mock
    private ObjectMapper objectMapper;

    @Test
    void objectToString_returnsEmptyWhenPayloadNull() {
        assertThat(JsonPayloadUtil.objectToString(new ObjectMapper(), null)).isEmpty();
    }

    @Test
    void objectToString_serializesPayload() {
        assertThat(JsonPayloadUtil.objectToString(new ObjectMapper(), Map.of("k", 1))).contains("k");
    }

    @Test
    void objectToString_propagatesWhenMapperFails() throws Exception {
        when(objectMapper.writeValueAsString("x")).thenThrow(new JsonProcessingException("x") {});
        assertThatThrownBy(() -> JsonPayloadUtil.objectToString(objectMapper, "x"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("JSON");
    }
}
