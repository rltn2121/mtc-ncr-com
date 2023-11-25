package core.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.dto.MtcExgRequest;
import core.dto.MtcNcrPayRequest;
import core.dto.MtcNcrSdaMainMasRequest;
import org.apache.commons.lang3.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

public class ComMessageDeserializer implements Deserializer<MtcNcrSdaMainMasRequest> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public MtcNcrSdaMainMasRequest deserialize(String s, byte[] bytes) {
        try {
            return objectMapper.readValue(new String(bytes), MtcNcrSdaMainMasRequest.class);
        } catch (JsonProcessingException e) {
            System.out.println(e.getOriginalMessage());
            throw new SerializationException(e);
        }
    }
}
