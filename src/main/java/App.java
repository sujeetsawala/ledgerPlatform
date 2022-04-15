import com.google.inject.Guice;
import com.google.inject.Injector;
import java.util.Scanner;
import resource.AdminResource;
import resource.BankOperationResource;

public class App {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new LedgerModule());
        AdminResource adminResource = injector.getInstance(AdminResource.class);
        adminResource.initialize("User/users.json");

        System.out.println("Enter path of the file containing bank operations: ");
        Scanner scanner = new Scanner(System.in);
        final String inputPath = scanner.nextLine();

        BankOperationResource bankOperationResource = injector.getInstance(BankOperationResource.class);
        bankOperationResource.execute(inputPath);

    }
}
