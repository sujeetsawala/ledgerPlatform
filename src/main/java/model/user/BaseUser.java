package model.user;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;


@Data
@NoArgsConstructor
@JsonTypeInfo(use = Id.NAME, property = "userType")
@JsonSubTypes({
        @JsonSubTypes.Type(name = "BANK", value = Bank.class),
        @JsonSubTypes.Type(name = "USER", value = User.class)
})
public abstract class BaseUser {
    private UserType userType;
    private String userId;
    private String name;
    private String address;

    public BaseUser(@NonNull final UserType userType) {
        this.userType = userType;
    }

    public BaseUser(@NonNull final UserType userType, @NonNull final String userId, @NonNull final String name, final String address) {
        this.userType = userType;
        this.address = address;
        this.userId = userId;
        this.name = name;
        this.address = address;
    }

    public abstract <T> T accept(final BaseUserVisitor<T> baseUserVisitor);

}
