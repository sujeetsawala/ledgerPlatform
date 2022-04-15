package model.user;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class User extends BaseUser {
    private String mobNo;

    public User() {
        super(UserType.USER);
    }

    @Builder(builderMethodName = "childBuilder")
    public User(final String userId, final String userName, final String address, final String mobNo) {
        super(UserType.USER, userId, userName, address);
        this.mobNo = mobNo;
    }

    @Override
    public <T> T accept(final BaseUserVisitor<T> baseUserVisitor) {
        return baseUserVisitor.visitUser(this);
    }
}
