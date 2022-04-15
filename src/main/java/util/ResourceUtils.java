package util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ResourceUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T getResource(File file, TypeReference<T> type) throws IOException {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.readValue(file, type);
    }

    public static <T> T getResource(String path, TypeReference<T> type) throws IOException {
        final InputStream data = ResourceUtils.class.getClassLoader()
                .getResourceAsStream(path);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.readValue(data, type);
    }
}
