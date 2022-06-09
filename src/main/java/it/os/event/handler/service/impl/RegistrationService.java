package it.os.event.handler.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.os.event.handler.entity.RegistrationRequest;
import it.os.event.handler.entity.UserETY;
import it.os.event.handler.enums.UserRole;
import it.os.event.handler.exception.BusinessException;
import it.os.event.handler.exception.UsernameAlreadyTakenException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RegistrationService {

    @Autowired
    private UserService userService;

    public void register(RegistrationRequest registrationRequest) {
        try {
            log.info("Registering user {}", registrationRequest.getUsername());
            userService.signUpUser(new UserETY(registrationRequest.getUsername(), registrationRequest.getPassword(),
                    UserRole.getUserRole(registrationRequest.getUserRole())));
        } catch (UsernameAlreadyTakenException usernameEx) {
            throw usernameEx;
        } catch (Exception e) {
            log.info("Error while registering user", e);
            throw new BusinessException("Error while registering user", e);
        }
    }

}
