package model.operations.entity;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import lombok.Data;
import lombok.NonNull;

import model.operations.OperationVisitor;
import model.operations.OperationType;

@Data
@JsonTypeInfo(use = Id.NAME, property = "requestType")
@JsonSubTypes({
        @JsonSubTypes.Type(name = "BALANCE", value = BalanceOperationEntity.class),
        @JsonSubTypes.Type(name = "PAYMENT", value = PaymentOperationEntity.class),
        @JsonSubTypes.Type(name = "LOAN", value = LoanOperationEntity.class)
})
public abstract class BankOperationEntity {

    private OperationType requestType;
    private String bankId;

    public BankOperationEntity(@NonNull final OperationType requestType) {
        this.requestType = requestType;
    }

    public BankOperationEntity(@NonNull final OperationType requestType, @NonNull final String bankId) {
        this.requestType = requestType;
        this.bankId = bankId;
    }

    public abstract <T> T accept(final OperationVisitor<T> operationVisitor);

}
