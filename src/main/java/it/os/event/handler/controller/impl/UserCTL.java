package it.os.event.handler.controller.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;

import it.os.event.handler.controller.IUserCTL;
import it.os.event.handler.entity.RegistrationRequest;
import it.os.event.handler.enums.UserRole;
import it.os.event.handler.exception.AdminRequiredException;
import it.os.event.handler.service.impl.RegistrationService;
import it.os.event.handler.service.impl.UserService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class UserCTL implements IUserCTL {
    
    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private UserService userService;

    private static final String ADMIN_REQUIRED_MSG = "To execute this operation an admin user is required";

    private boolean isAdminUser() {
        boolean isAdmin = false;
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserDetails) {
                UserDetails detailUser = ((UserDetails) principal);
                isAdmin = detailUser.getAuthorities().toString().contains(UserRole.ADMIN.name());
            }
        } catch (Exception e) {
            log.error("User not authenticated as admin or not authenticated at all");
        }
        return isAdmin;
    }

    @Override
    public ResponseEntity<String> createUser(RegistrationRequest registrationRequest, HttpServletRequest request) {

        log.info("Creating a user with username: {}", registrationRequest.getUsername());
        if (!isAdminUser()) {
            throw new AdminRequiredException(ADMIN_REQUIRED_MSG);
        }
        registrationService.register(registrationRequest);
        return new ResponseEntity<>("User created", HttpStatus.OK);
    }

    @Override
    public UserDetails getUser(String username, HttpServletRequest request) {
        if (!isAdminUser()) {
            throw new AdminRequiredException(ADMIN_REQUIRED_MSG);
        }
        return userService.loadUserByUsername(username);
    }

    @Override
    public List<UserDetails> getAllUsers(HttpServletRequest request) {
        if (!isAdminUser()) {
            throw new AdminRequiredException(ADMIN_REQUIRED_MSG);
        }

        return userService.getAllUsers();
    }

    @Override
    public ResponseEntity<String> deleteUser(String username, HttpServletRequest request) {
        
        if (!isAdminUser()) {
            throw new AdminRequiredException(ADMIN_REQUIRED_MSG);
        }
        userService.deleteUser(username);
        return new ResponseEntity<>("User deleted", HttpStatus.OK);
    }

}
