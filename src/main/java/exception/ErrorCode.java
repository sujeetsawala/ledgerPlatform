package exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INVALID_BANK_ID(400),
    INVALID_USER_ID(400),
    INVALID_LOAN_ID( 400);

    private final int responseCode;

}
