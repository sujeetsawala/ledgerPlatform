package model.operations.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import model.operations.OperationType;
import model.operations.OperationVisitor;

@Data
@EqualsAndHashCode (callSuper = true)
public class PaymentOperationEntity extends BankOperationEntity {
    @NonNull
    private String loanId;
    @NonNull
    private String paymentId;
    private String payerId;
    private double lumpsumAmount;
    private int emiNo;

    public PaymentOperationEntity() {
        super(OperationType.PAYMENT);
    }

    @Builder (builderMethodName = "childBuilder")
    public PaymentOperationEntity(final String payeeId, final String loanId, final String paymentId, final String payerId, final double lumpsumAmount, final int emiNo) {
        super(OperationType.PAYMENT, payeeId);
        this.loanId = loanId;
        this.paymentId = paymentId;
        this.payerId = payerId;
        this.lumpsumAmount = lumpsumAmount;
        this.emiNo = emiNo;
    }

    @Override
    public <T> T accept(OperationVisitor<T> operationVisitor) {
        return operationVisitor.visitPayment(this);
    }
}
