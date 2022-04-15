package service.visitor;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import model.user.Bank;
import model.user.BaseUser;
import model.user.BaseUserVisitor;
import model.user.User;
import service.UserService;

public class UserVisitor implements BaseUserVisitor<Void> {
    private final UserService userServiceImpl;

    @Inject
    public UserVisitor(final UserService userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @Override
    public Void visitUser(final BaseUser baseUser) {
        User bank = (User) baseUser;
        userServiceImpl.addUser(bank.getUserId(), bank.getName());
        return null;
    }

    @Override
    public Void visitBank(final BaseUser baseUser) {
        Bank bank = (Bank) baseUser;
        userServiceImpl.addBank(bank.getUserId(), bank.getName());
        return null;
    }
}
