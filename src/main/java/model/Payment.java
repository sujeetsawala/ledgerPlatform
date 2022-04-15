package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    @NonNull
    private String paymentId;
    @NonNull
    private PaymentType paymentType;
    @NonNull
    private String payeeId;
    @NonNull
    private String payerId;
    private double amount;
    private int emiNo;
}
