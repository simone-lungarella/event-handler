package it.os.event.handler.enums;

public enum UserRole {
    USER,
    ADMIN;

    public static UserRole getUserRole(String role) {
        if (role.equals("USER")) {
            return UserRole.USER;
        } else if (role.equals("ADMIN")) {
            return UserRole.ADMIN;
        } else {
            throw new IllegalArgumentException("Invalid role: " + role);
        }
    }
}
