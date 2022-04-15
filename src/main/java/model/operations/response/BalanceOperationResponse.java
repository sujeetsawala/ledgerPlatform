package model.operations.response;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import model.operations.OperationType;

@Data
@EqualsAndHashCode(callSuper = true)
public class BalanceOperationResponse extends BankOperationResponse {
    private String borrowerName;
    private double amountPaid;
    private int noOfEmiLeft;

    public BalanceOperationResponse() {
        super(OperationType.BALANCE);
    }

    @Builder(builderMethodName = "childBuilder")
    public BalanceOperationResponse(final String bankName, final double amountPaid, final String borrowerName, final int noOfEmiLeft) {
        super(OperationType.BALANCE, bankName);
        this.amountPaid = amountPaid;
        this.borrowerName = borrowerName;
        this.noOfEmiLeft = noOfEmiLeft;
    }
}
