package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Loan {
    @NonNull
    private String loanId;
    private double principalAmount;
    private double totalInterest;
    private double totalAmount;
    private int rateOfInterest;
    private int timePeriod;
    private long monthlyEmi;
}
