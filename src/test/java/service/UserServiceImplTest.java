package service;

import org.junit.Assert;
import org.junit.Test;
import service.impl.UserServiceImpl;

public class UserServiceImplTest {
    private final String bankId = "B122";
    private final String userId = "U122";
    private final String bankName = "Bank122";
    private final String userName = "User122";
    private final UserService userService = new UserServiceImpl();

    @Test
    public void addBank() {
       userService.addBank(bankId, bankName);
       Assert.assertNotNull(userService.getUser(bankId));
    }

    @Test
    public void addUser() {
        userService.addBank(userId, userName);
        Assert.assertNotNull(userService.getUser(userId));
    }
}
