package service;

import model.Loan;
import model.Payment;
import model.operations.entity.BalanceOperationEntity;
import model.operations.entity.LoanOperationEntity;
import model.operations.entity.PaymentOperationEntity;
import model.operations.response.BalanceOperationResponse;

public interface LoanService {

    void createLoan(LoanOperationEntity loanOperationEntity);

    BalanceOperationResponse getLoanDetails(BalanceOperationEntity balanceOperationEntity);

    void payLumpsumLoanAmount(PaymentOperationEntity paymentOperationEntity);

    Loan getLoan(final String loanId);

    Payment getPaymentForLoan(final String loanId, final String paymentId);
}