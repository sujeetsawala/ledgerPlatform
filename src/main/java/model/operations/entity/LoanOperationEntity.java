package model.operations.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import model.operations.OperationType;
import model.operations.OperationVisitor;

@Data
@EqualsAndHashCode(callSuper = true)
public class LoanOperationEntity extends BankOperationEntity {
    @NonNull
    private String loanId;
    private String borrowerId;
    private double principalAmount;
    private int rateOfInterest;
    private int timePeriod;

    public LoanOperationEntity() {
        super(OperationType.LOAN);
    }

    @Builder(builderMethodName = "childBuilder")
    public LoanOperationEntity(final String bankId, final String borrowerId, final String loanId, final double principalAmount, final int rateOfInterest, final int timePeriod) {
        super(OperationType.LOAN, bankId);
        this.loanId = loanId;
        this.borrowerId = borrowerId;
        this.principalAmount = principalAmount;
        this.rateOfInterest = rateOfInterest;
        this.timePeriod = timePeriod;
    }

    @Override
    public <T> T accept(OperationVisitor<T> operationVisitor) {
        return operationVisitor.visitLoan(this);
    }
}
