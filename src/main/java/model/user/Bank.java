package model.user;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Bank extends BaseUser{

    public Bank() {
        super(UserType.BANK);
    }

    @Builder(builderMethodName = "childBuilder")
    public Bank(final String bankId, final String bankName, final String address) {
        super(UserType.BANK, bankId, bankName, address);
    }

    @Override
    public <T> T accept(final BaseUserVisitor<T> baseUserVisitor) {
        return baseUserVisitor.visitBank(this);
    }

}