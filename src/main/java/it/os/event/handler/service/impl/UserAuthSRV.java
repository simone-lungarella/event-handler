package it.os.event.handler.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.os.event.handler.repository.IUserAuthRepo;
import it.os.event.handler.service.IUserAuthSRV;

@Service
public class UserAuthSRV implements IUserAuthSRV {

    @Autowired
    private IUserAuthRepo userAuthRepo;

    @Override
    public String getUserAuth(String username) {
        return userAuthRepo.getUserAuth(username);
    }

    @Override
    public void updateUserAuth(String username, String auth) {
        userAuthRepo.updateUserAuth(username, auth);
    }
}
