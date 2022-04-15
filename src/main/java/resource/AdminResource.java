package resource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import java.io.IOException;
import java.util.List;
import model.user.BaseUser;
import model.user.BaseUserVisitor;
import util.ResourceUtils;

@Singleton
public class AdminResource {

    private final BaseUserVisitor<Void> userVisitor;

    @Inject
    public AdminResource(final BaseUserVisitor<Void> userVisitor) {
        this.userVisitor = userVisitor;
    }

    public void initialize(String inputPath) {
        try {
            List<BaseUser> baseUsers = ResourceUtils.getResource(inputPath, new TypeReference<List<BaseUser>>() {
            });
            this.addBank(baseUsers);
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    public void addBank(List<BaseUser> baseUsers) {
        for (BaseUser baseUser : baseUsers) {
            baseUser.accept(userVisitor);
        }
    }
}
