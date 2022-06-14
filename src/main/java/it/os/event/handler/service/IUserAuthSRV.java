package it.os.event.handler.service;

public interface IUserAuthSRV {
    
    String getUserAuth(String username);

    void updateUserAuth(String username, String auth);

}
