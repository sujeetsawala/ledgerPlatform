package model.operations.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import model.operations.OperationType;
import model.operations.OperationVisitor;

@Data
@EqualsAndHashCode(callSuper = true)
public class BalanceOperationEntity extends BankOperationEntity {

    private String borrowerId;
    @NonNull
    private String loanId;
    private int emiNo;

    public BalanceOperationEntity() {
        super(OperationType.BALANCE);
    }

    @Builder(builderMethodName = "childBuilder")
    public BalanceOperationEntity(final String bankId, final String loanId, final String borrowerId, final int emiNo) {
        super(OperationType.BALANCE, bankId);
        this.loanId = loanId;
        this.borrowerId = borrowerId;
        this.emiNo = emiNo;
    }

    @Override
    public <T> T accept(OperationVisitor<T> operationVisitor) {
        return operationVisitor.visitBalance(this);
    }
}
