package model.operations;

import model.operations.entity.BalanceOperationEntity;
import model.operations.entity.LoanOperationEntity;
import model.operations.entity.PaymentOperationEntity;

public interface OperationVisitor<T> {
    T visitBalance(BalanceOperationEntity request);

    T visitPayment(PaymentOperationEntity request);

    T visitLoan(LoanOperationEntity request);
}
