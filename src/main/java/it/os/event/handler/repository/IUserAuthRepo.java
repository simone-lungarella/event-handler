package it.os.event.handler.repository;

public interface IUserAuthRepo {
    
    String getUserAuth(String username);

    void updateUserAuth(String username, String auth);
}
