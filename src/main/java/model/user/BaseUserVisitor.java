package model.user;


public interface BaseUserVisitor<T> {
    T visitUser(BaseUser baseUser);

    T visitBank(BaseUser baseUser);
}
