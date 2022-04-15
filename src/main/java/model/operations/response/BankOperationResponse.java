package model.operations.response;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import lombok.Data;
import lombok.NonNull;
import model.operations.OperationType;

@Data
@JsonTypeInfo(use = Id.NAME, property = "responseType")
@JsonSubTypes({
        @JsonSubTypes.Type(name = "BALANCE", value = BalanceOperationResponse.class)
})
public abstract class BankOperationResponse {
    private OperationType responseType;
    private String bankName;

    public BankOperationResponse(@NonNull final OperationType responseType) {
        this.responseType = responseType;
    }

    public BankOperationResponse(@NonNull final OperationType responseType, final String bankName) {
        this.responseType = responseType;
        this.bankName = bankName;
    }
}
