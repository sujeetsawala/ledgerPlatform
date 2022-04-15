package model.user;

public enum UserType {
    USER {
        public <T> T accept(UserType.Visitor<T> visitor) {
            return visitor.visitUser();
        };
    },
    BANK {
        public <T> T accept(UserType.Visitor<T> visitor) {
            return visitor.visitBank();
        };
    };

    public abstract <T> T accept(UserType.Visitor<T> visitor);

    public interface Visitor<T> {
        T visitUser();

        T visitBank();
    }
}
