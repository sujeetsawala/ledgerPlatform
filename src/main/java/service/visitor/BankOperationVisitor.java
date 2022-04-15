package service.visitor;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import model.operations.OperationVisitor;
import model.operations.entity.BalanceOperationEntity;
import model.operations.entity.LoanOperationEntity;
import model.operations.entity.PaymentOperationEntity;
import model.operations.response.BankOperationResponse;
import service.LoanService;

public class BankOperationVisitor implements OperationVisitor<BankOperationResponse> {

    private final LoanService loanServiceImpl;

    @Inject
    public BankOperationVisitor(final LoanService loanServiceImpl) {
        this.loanServiceImpl = loanServiceImpl;
    }

    public BankOperationResponse visitBalance(BalanceOperationEntity request) {
        return loanServiceImpl.getLoanDetails(request);
    }

    public BankOperationResponse visitPayment(PaymentOperationEntity request) {
        this.loanServiceImpl.payLumpsumLoanAmount(request);
        return null;
    }

    public BankOperationResponse visitLoan(LoanOperationEntity request) {
       this.loanServiceImpl.createLoan(request);
       return null;
    }
}
