import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import model.operations.OperationVisitor;
import model.operations.response.BankOperationResponse;
import model.user.BaseUserVisitor;
import service.LoanService;
import service.UserService;
import service.impl.LoanServiceImpl;
import service.impl.UserServiceImpl;
import service.visitor.BankOperationVisitor;
import service.visitor.UserVisitor;

public class LedgerModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(new TypeLiteral<BaseUserVisitor<Void>>(){}).to(UserVisitor.class).in(Scopes.SINGLETON);
        bind(new TypeLiteral<OperationVisitor<BankOperationResponse>>(){}).to(BankOperationVisitor.class).in(Scopes.SINGLETON);
        bind(UserService.class).to(UserServiceImpl.class).in(Scopes.SINGLETON);
        bind(LoanService.class).to(LoanServiceImpl.class).in(Scopes.SINGLETON);
    }
}
