package core.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.dto.MtcNcrUpdateMainMasRequest;
import org.apache.commons.lang3.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

public class ComMessageDeserializer implements Deserializer<MtcNcrUpdateMainMasRequest> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public MtcNcrUpdateMainMasRequest deserialize(String s, byte[] bytes) {
        try {
            return objectMapper.readValue(new String(bytes), MtcNcrUpdateMainMasRequest.class);
        } catch (JsonProcessingException e) {
            System.out.println(e.getOriginalMessage());
            throw new SerializationException(e);
        }
    }
}
