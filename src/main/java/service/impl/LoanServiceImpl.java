package service.impl;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import exception.ErrorCode;
import exception.LedgerException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import model.Loan;
import model.Payment;
import model.PaymentType;
import model.operations.entity.BalanceOperationEntity;
import model.operations.entity.LoanOperationEntity;
import model.operations.entity.PaymentOperationEntity;
import model.operations.response.BalanceOperationResponse;
import model.user.Bank;
import model.user.User;
import service.LoanService;
import service.UserService;

public class LoanServiceImpl implements LoanService {

    private final HashMap<String, Loan> loanDetails;
    private final HashMap<String, List<Payment>> paymentDetails;
    private final UserService userServiceImpl;

    @Inject
    public LoanServiceImpl(final UserService userServiceImpl) {
        this.loanDetails = new HashMap<>();
        this.paymentDetails = new HashMap<>();
        this.userServiceImpl = userServiceImpl;
    }

    @Override
    public void createLoan(LoanOperationEntity entity) {
        final double totalInterest = this.calculateInterest(entity.getPrincipalAmount(), entity.getTimePeriod(),
                entity.getRateOfInterest());
        final double totalAmount = entity.getPrincipalAmount() + totalInterest;
        final long monthlyEmi = this.calculateMonthlyEmi(totalAmount, entity.getTimePeriod());

        this.validateBank(entity.getBankId());
        this.validateBorrower(entity.getBorrowerId());

        final Loan loan = Loan.builder()
                .loanId(entity.getLoanId())
                .principalAmount(entity.getPrincipalAmount())
                .rateOfInterest(entity.getRateOfInterest())
                .timePeriod(entity.getTimePeriod())
                .totalAmount(totalAmount)
                .totalInterest(totalInterest)
                .monthlyEmi(monthlyEmi)
                .build();
        loanDetails.put(entity.getLoanId(), loan);
    }

    @Override
    public void payLumpsumLoanAmount(PaymentOperationEntity entity) {
        final Payment payment = Payment.builder()
                .paymentId(entity.getPaymentId())
                .paymentType(PaymentType.LUMPSUM)
                .payeeId(entity.getBankId())
                .payerId(entity.getPayerId())
                .amount(entity.getLumpsumAmount())
                .emiNo(entity.getEmiNo())
                .build();

        this.validateBorrower(entity.getPayerId());
        this.validateBank(entity.getBankId());
        this.validateLoan(entity.getLoanId());

        if (paymentDetails.containsKey(entity.getLoanId())) {
            paymentDetails.get(entity.getLoanId())
                    .add(payment);
        } else {
            paymentDetails.put(entity.getLoanId(), new ArrayList<Payment>() {
                {
                    add(payment);
                }
            });
        }
    }

    @Override
    public BalanceOperationResponse getLoanDetails(BalanceOperationEntity entity) {
        final int emiNo = entity.getEmiNo();

        this.validateBorrower(entity.getBorrowerId());
        this.validateBank(entity.getBankId());
        this.validateLoan(entity.getLoanId());

        final String loanId = entity.getLoanId();
        final Loan loan = loanDetails.get(loanId);
        final Bank bank = (Bank) userServiceImpl.getUser(entity.getBankId());
        final User borrower = (User) userServiceImpl.getUser(entity.getBorrowerId());

        List<Payment> payments = paymentDetails.get(loanId);
        final double lumpsumAmountPaid = Objects.isNull(payments) ? 0.0 : payments.stream()
                .filter(e -> e.getEmiNo() <= emiNo)
                .map(e -> e.getAmount())
                .reduce(0.0, (a, b) -> a + b);

        final double monthlyEmi = loan.getMonthlyEmi();
        final double totalPayableAmount = loan.getTotalAmount();
        final double residueAmount = Math.max(0.0, totalPayableAmount - lumpsumAmountPaid - monthlyEmi * emiNo);
        final int noOfEmiLeft = (int)Math.ceil(residueAmount / monthlyEmi);
        return BalanceOperationResponse.childBuilder()
                .amountPaid(Math.min(totalPayableAmount, lumpsumAmountPaid + emiNo * monthlyEmi))
                .bankName(bank.getName())
                .borrowerName(borrower.getName())
                .noOfEmiLeft(noOfEmiLeft)
                .build();

    }

    @Override
    public Loan getLoan(final String loanId) {
        if (loanDetails.containsKey(loanId)) {
            return loanDetails.get(loanId);
        }
        return null;
    }

    @Override
    public Payment getPaymentForLoan(final String loanId, final String paymentId) {
        if (paymentDetails.containsKey(loanId)) {
            Optional<Payment> payment = paymentDetails.get(loanId)
                    .stream()
                    .filter(e -> e.getPaymentId().equals(paymentId))
                    .findFirst();
           return (payment.isPresent() ? payment.get() : null);
        }
        return null;
    }


    private double calculateInterest(double principalAmount, int timePeriod, int rateOfInterest) {
        return (principalAmount * timePeriod * rateOfInterest / 100.0);
    }

    private long calculateMonthlyEmi(double totalAmount, int timePeriod) {
        return (long) Math.ceil(totalAmount / (timePeriod * 12));
    }

    private void validateLoan(final String loanId) {
        final Loan loan = loanDetails.get(loanId);
        if (loan == null) {
            throw LedgerException.error(ErrorCode.INVALID_LOAN_ID, ImmutableMap.of("message", "Invalid Loan Id"));
        }
    }

    private void validateBank(final String bankId) {
        final Bank bank = (Bank) userServiceImpl.getUser(bankId);
        if (bank == null) {
            throw LedgerException.error(ErrorCode.INVALID_LOAN_ID, ImmutableMap.of("message", "Invalid Loan Id"));
        }
    }

    private void validateBorrower(final String borrowerId) {
        final User borrower = (User) userServiceImpl.getUser(borrowerId);
        if (borrower == null) {
            throw LedgerException.error(ErrorCode.INVALID_LOAN_ID, ImmutableMap.of("message", "Invalid Loan Id"));
        }
    }
}
