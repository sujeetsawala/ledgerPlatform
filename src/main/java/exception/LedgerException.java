package exception;

import com.google.common.collect.ImmutableMap;
import java.util.Collections;
import java.util.Map;
import lombok.Getter;

@Getter
public class LedgerException extends RuntimeException {
    private final ErrorCode errorCode;
    private final Map<String, Object> context;

    private LedgerException(ErrorCode errorCode, Map<String, Object> context) {
        super(errorCode.name());
        this.errorCode = errorCode;
        this.context = context;
    }

    public static LedgerException error(ErrorCode errorCode) {
        return new LedgerException(errorCode, ImmutableMap.of());
    }

    public static LedgerException error(ErrorCode errorCode, Map<String, Object> context) {
        return new LedgerException(errorCode, context);
    }

    public static LedgerException error(String message, ErrorCode errorCode) {
        return error(errorCode, Collections.singletonMap("message", message));
    }
}
