package bo.com.bg.domain.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

@UtilityClass
public class JsonPayloadUtil {

    public String objectToString(ObjectMapper mapper, Object payload) {
        if (payload == null) {
            return "";
        }
        try {
            return mapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("No se pudo serializar el payload a JSON", e);
        }
    }
}
