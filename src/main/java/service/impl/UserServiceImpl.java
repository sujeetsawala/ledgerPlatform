package service.impl;

import java.util.HashMap;
import model.user.Bank;
import model.user.BaseUser;
import model.user.User;
import service.UserService;

public class UserServiceImpl implements UserService {

    private final HashMap<String, BaseUser> userDetails;

    public UserServiceImpl() {
        this.userDetails = new HashMap<>();
    }

    // Taking ids in the request for simplicity purpose
    @Override
    public void addBank(final String bankId, final String bankName) {
        Bank bank = Bank.childBuilder()
                .bankId(bankId)
                .bankName(bankName)
                .build();

        userDetails.put(bankId, bank);
    }

    @Override
    public void addUser(final String userId, final String userName) {
        User user = User.childBuilder()
                .userId(userId)
                .userName(userName)
                .build();

        userDetails.put(userId, user);
    }

    @Override
    public BaseUser getUser(String userId) {
        if (userDetails.containsKey(userId)) {
            return userDetails.get(userId);
        }
        return null;
    }
}
