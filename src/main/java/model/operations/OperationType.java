package model.operations;

public enum OperationType {
    PAYMENT {
        public <T> T accept(Visitor<T> visitor) {
           return visitor.visitPayment();
        };
    },
    BALANCE {
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitBalance();
        };
    },
    LOAN {
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitLoan();
        };
    };

    public abstract <T> T accept(Visitor<T> visitor);

    public interface Visitor<T> {
        T visitPayment();

        T visitBalance();

        T visitLoan();
    }
}
