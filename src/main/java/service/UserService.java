package service;

import model.user.BaseUser;

public interface UserService {
    void addBank(final String bankId, String bankName);

    void addUser(final String userId, String userName);

    BaseUser getUser(final String userId);
}
