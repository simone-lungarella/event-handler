package it.os.event.handler.controller.impl;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import it.os.event.handler.enums.UserRole;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractCTL {
    
    protected boolean isAdminUser() {
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
}
