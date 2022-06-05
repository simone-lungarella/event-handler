package it.os.event.handler.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class RegistrationRequest {

    private final String username;

    private final String password;

    private final String userRole;

}
