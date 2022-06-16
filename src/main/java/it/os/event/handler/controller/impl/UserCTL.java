package it.os.event.handler.controller.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RestController;

import it.os.event.handler.controller.IUserCTL;
import it.os.event.handler.entity.RegistrationRequest;
import it.os.event.handler.exception.AdminRequiredException;
import it.os.event.handler.service.IUserAuthSRV;
import it.os.event.handler.service.impl.RegistrationService;
import it.os.event.handler.service.impl.UserService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class UserCTL extends AbstractCTL implements IUserCTL {

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private UserService userService;

    @Autowired
    private IUserAuthSRV userAuthService;

    private static final String ADMIN_REQUIRED_MSG = "To execute this operation an admin user is required";

    @Override
    public ResponseEntity<String> createUser(final RegistrationRequest registrationRequest,
            final HttpServletRequest request) {

        log.info("Creating a user with username: {}", registrationRequest.getUsername());
        registrationService.register(registrationRequest);
        return new ResponseEntity<>("User created", HttpStatus.OK);
    }

    @Override
    public UserDetails getUser(final String username, final HttpServletRequest request) {
        if (!isAdminUser()) {
            throw new AdminRequiredException(ADMIN_REQUIRED_MSG);
        }
        return userService.loadUserByUsername(username);
    }

    @Override
    public List<UserDetails> getAllUsers(final HttpServletRequest request) {
        if (!isAdminUser()) {
            throw new AdminRequiredException(ADMIN_REQUIRED_MSG);
        }

        return userService.getAllUsers();
    }

    @Override
    public ResponseEntity<String> deleteUser(final String username, final HttpServletRequest request) {

        if (!isAdminUser()) {
            throw new AdminRequiredException(ADMIN_REQUIRED_MSG);
        }
        userService.deleteUser(username);
        return new ResponseEntity<>("User deleted", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> getUserAuth(final String username, final HttpServletRequest request) {

        final String authorizations = userAuthService.getUserAuth(username);

        if (StringUtils.isEmpty(authorizations)) {
            throw new UsernameNotFoundException(
                    String.format("Authorization with username %s has no authorizations", username));
        } else {
            return new ResponseEntity<>(authorizations, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<Void> updateUserAuth(String username, String auth, HttpServletRequest request) {
        log.info("Updating authorization for user {}", username);
        userAuthService.updateUserAuth(username, auth);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
