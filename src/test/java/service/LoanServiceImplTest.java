package service;

import static org.mockito.ArgumentMatchers.eq;

import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;
import model.Loan;
import model.Payment;
import model.operations.OperationType.Visitor;
import model.operations.entity.BalanceOperationEntity;
import model.operations.entity.BankOperationEntity;
import model.operations.entity.LoanOperationEntity;
import model.operations.entity.PaymentOperationEntity;
import model.operations.response.BalanceOperationResponse;
import model.operations.response.BankOperationResponse;
import model.user.Bank;
import model.user.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import service.impl.LoanServiceImpl;
import util.ResourceUtils;

public class LoanServiceImplTest {

    private final String loanId = "L123";
    private final String paymentId = "P123";
    private final String bankId = "B122";
    private final String borrowerId = "U122";
    private final String borrowerName = "User122";
    private final String bankName = "Bank122";
    private final double delta = 0.00000;

    @Mock
    private UserService userService;

    @InjectMocks
    private LoanServiceImpl loanServiceImpl;

    private LoanOperationEntity dummyLoanOperationEntity;

    private PaymentOperationEntity dummyPaymentOperationEntity;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(userService.getUser(eq(bankId))).thenReturn(Bank.childBuilder().bankId(bankId).bankName(bankName).build());
        Mockito.when(userService.getUser(eq(borrowerId))).thenReturn(User.childBuilder().userId(borrowerId).userName(borrowerName).build());

        double principalAmount = 10000;
        int rateOfInterest = 4;
        int timePeriod = 5;
        this.dummyLoanOperationEntity = LoanOperationEntity
                .childBuilder()
                .bankId(bankId)
                .borrowerId(borrowerId)
                .loanId(loanId)
                .principalAmount(principalAmount)
                .rateOfInterest(rateOfInterest)
                .timePeriod(timePeriod)
                .build();

        this.dummyPaymentOperationEntity = PaymentOperationEntity
                .childBuilder()
                .paymentId(paymentId)
                .payeeId(bankId)
                .payerId(borrowerId)
                .lumpsumAmount(1000)
                .loanId(loanId)
                .emiNo(10)
                .build();
    }

    @Test
    public void createLoan() {
        loanServiceImpl.createLoan(dummyLoanOperationEntity);
        Loan dummyLoan = loanServiceImpl.getLoan(loanId);

        Assert.assertEquals(dummyLoanOperationEntity.getLoanId(), dummyLoan.getLoanId());
        Assert.assertEquals(dummyLoanOperationEntity.getPrincipalAmount(), dummyLoan.getPrincipalAmount(), delta);
        Assert.assertEquals(dummyLoanOperationEntity.getRateOfInterest(), dummyLoan.getRateOfInterest(), delta);
        Assert.assertEquals(dummyLoanOperationEntity.getTimePeriod(), dummyLoan.getTimePeriod(), delta);
    }

    @Test
    public void payLumpsumLoanAmount() {

        loanServiceImpl.createLoan(dummyLoanOperationEntity);
        loanServiceImpl.payLumpsumLoanAmount(dummyPaymentOperationEntity);
        Payment payment = loanServiceImpl.getPaymentForLoan(loanId, paymentId);
        Assert.assertEquals(dummyPaymentOperationEntity.getPaymentId(), payment.getPaymentId());
        Assert.assertEquals(dummyPaymentOperationEntity.getPayerId(), payment.getPayerId());
        Assert.assertEquals(dummyPaymentOperationEntity.getLumpsumAmount(), payment.getAmount(), delta);
        Assert.assertEquals(dummyPaymentOperationEntity.getEmiNo(), payment.getEmiNo());
    }

    @Test
    public void getLoanDetails() {
        loanServiceImpl.createLoan(dummyLoanOperationEntity);
        loanServiceImpl.payLumpsumLoanAmount(dummyPaymentOperationEntity);
        final BalanceOperationEntity balanceOperationEntity = BalanceOperationEntity
                .childBuilder()
                .bankId(bankId)
                .borrowerId(borrowerId)
                .loanId(loanId)
                .emiNo(5)
                .build();

        BalanceOperationResponse balanceOperationResponse = loanServiceImpl.getLoanDetails(balanceOperationEntity);
        Assert.assertEquals(1000, balanceOperationResponse.getAmountPaid() , delta);
        Assert.assertEquals(borrowerName, balanceOperationResponse.getBorrowerName());
        Assert.assertEquals(55, balanceOperationResponse.getNoOfEmiLeft());
        Assert.assertEquals(bankName, balanceOperationResponse.getBankName());
    }

    @Test
    public void getLoanDetailsBasedOnInput() {
        try {
            List<BankOperationEntity> bankOperationEntities = ResourceUtils.getResource("input/Input3.json", new TypeReference<List<BankOperationEntity>>() {});
            List<BalanceOperationResponse> balanceOperationResponses = ResourceUtils.getResource("output/Output3.json", new TypeReference<List<BalanceOperationResponse>>() {});

            final int[] index = {0};
            for(BankOperationEntity bankOperationEntity: bankOperationEntities) {
                bankOperationEntity.getRequestType().accept(new Visitor<BankOperationResponse>() {
                    @Override
                    public BankOperationResponse visitPayment() {
                        PaymentOperationEntity paymentOperationEntity = (PaymentOperationEntity) bankOperationEntity;
                        loanServiceImpl.payLumpsumLoanAmount(paymentOperationEntity);
                        Payment payment = loanServiceImpl.getPaymentForLoan(loanId, paymentId);
                        Assert.assertEquals(paymentOperationEntity.getPaymentId(), payment.getPaymentId());
                        Assert.assertEquals(paymentOperationEntity.getPayerId(), payment.getPayerId());
                        Assert.assertEquals(paymentOperationEntity.getLumpsumAmount(), payment.getAmount(), delta);
                        Assert.assertEquals(paymentOperationEntity.getEmiNo(), payment.getEmiNo());
                        return null;
                    }

                    @Override
                    public BankOperationResponse visitBalance() {
                        BalanceOperationEntity balanceOperationEntity = (BalanceOperationEntity) bankOperationEntity;
                        BalanceOperationResponse balanceOperationResponse = loanServiceImpl.getLoanDetails(balanceOperationEntity);
                        BalanceOperationResponse expectedBalanceResponse = balanceOperationResponses.get(index[0]);
                        index[0] = index[0] + 1;
                        Assert.assertEquals(expectedBalanceResponse.getAmountPaid(), balanceOperationResponse.getAmountPaid(), delta);
                        Assert.assertEquals(expectedBalanceResponse.getBorrowerName(), balanceOperationResponse.getBorrowerName());
                        Assert.assertEquals(expectedBalanceResponse.getNoOfEmiLeft(), balanceOperationResponse.getNoOfEmiLeft());
                        Assert.assertEquals(expectedBalanceResponse.getBankName(),balanceOperationResponse.getBankName());
                        return balanceOperationResponse;
                    }

                    @Override
                    public BankOperationResponse visitLoan() {
                        LoanOperationEntity loanOperationEntity = (LoanOperationEntity) bankOperationEntity;
                        loanServiceImpl.createLoan(loanOperationEntity);
                        Loan dummyLoan = loanServiceImpl.getLoan(loanId);
                        Assert.assertEquals(loanOperationEntity.getLoanId(), dummyLoan.getLoanId());
                        Assert.assertEquals(loanOperationEntity.getPrincipalAmount(), dummyLoan.getPrincipalAmount(), delta);
                        Assert.assertEquals(loanOperationEntity.getRateOfInterest(), dummyLoan.getRateOfInterest(), delta);
                        Assert.assertEquals(loanOperationEntity.getTimePeriod(), dummyLoan.getTimePeriod(), delta);
                        return null;
                    }
                });
            }
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }
}
