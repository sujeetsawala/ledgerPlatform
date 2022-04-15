package resource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import model.operations.OperationVisitor;
import model.operations.entity.BankOperationEntity;
import model.operations.response.BalanceOperationResponse;
import model.operations.response.BankOperationResponse;
import util.ResourceUtils;

@Singleton
public class BankOperationResource {
    private final OperationVisitor<BankOperationResponse> bankOperationVisitor;

    @Inject
    public BankOperationResource(final OperationVisitor<BankOperationResponse> bankOperationVisitor) {
        this.bankOperationVisitor = bankOperationVisitor;
    }

    public void execute(final String inputPath) {
        try {
            final List<BankOperationEntity> bankOperationEntities =  ResourceUtils.getResource(new File(inputPath), new TypeReference<List<BankOperationEntity>>() {});
            final List<BalanceOperationResponse> responses = new ArrayList<>();
            for(BankOperationEntity bankOperationEntity: bankOperationEntities) {
                BankOperationResponse response =  bankOperationEntity.accept(bankOperationVisitor);
                if(response != null) {
                    responses.add((BalanceOperationResponse) response);
                }
            }
            this.printResponse(responses);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    private void printResponse(final List<BalanceOperationResponse> bankOperationResponses) {

        final String displayTemplate = String.format("%s %s %s %s", "BankName", "BorrowerName", "AmountPaid", "NoOfEmiLeft");
        System.out.println(displayTemplate);
        for(BalanceOperationResponse balanceOperationResponse: bankOperationResponses) {
            final String outputTemplate = String.format("%s %10s %11s %8d", balanceOperationResponse.getBankName(), balanceOperationResponse.getBorrowerName(), balanceOperationResponse.getAmountPaid(), balanceOperationResponse.getNoOfEmiLeft());
            System.out.println(outputTemplate);
        }
    }
}
