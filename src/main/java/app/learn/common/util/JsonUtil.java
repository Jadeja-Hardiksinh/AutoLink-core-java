package app.learn.common.util;

import app.learn.common.enums.JsonStatusKey;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.Map;

public class JsonUtil {
    private static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public static ObjectMapper getObjectMapper() {

        return mapper;
    }

    public static <T> String createJson(String message, JsonStatusKey key, T object, String[] ignoreFields) {
        ObjectMapper mapper = getObjectMapper();
        ObjectNode mainNode = mapper.createObjectNode();
        if (message != null) {
            mainNode.put("message", message);
        }
        if (key != null) {
            mainNode.put("status", key.name());

        }
        if (object != null) {
            ObjectNode objNode = mapper.valueToTree(object);
            if (ignoreFields != null) {
                for (String field : ignoreFields) {
                    objNode.remove(field);
                }
                mainNode.set("data", objNode);
            }
        }

        return mainNode.toString();
    }

    public static <T> String createJson(Map<String, String> keys, T object, String[] ignoreFields) {
        ObjectMapper mapper = getObjectMapper();
        ObjectNode mainNode = mapper.createObjectNode();
        if (keys != null) {
            for (Map.Entry<String, String> entry : keys.entrySet()) {
                mainNode.put(entry.getKey(), entry.getValue());
            }
        }

        if (object != null) {
            ObjectNode objNode = mapper.valueToTree(object);
            if (ignoreFields != null) {
                for (String field : ignoreFields) {
                    objNode.remove(field);
                }
                mainNode.set("data", objNode);
            }
        }

        return mainNode.toString();
    }


}
