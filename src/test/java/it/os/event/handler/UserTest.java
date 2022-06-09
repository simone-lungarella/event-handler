package it.os.event.handler;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import it.os.event.handler.entity.RegistrationRequest;
import it.os.event.handler.enums.UserRole;
import it.os.event.handler.service.impl.RegistrationService;

@SpringBootTest(properties = { "spring.datasource.url=jdbc:h2:file:./data/event-handler-test-db" })
class UserTest {
    
    @Autowired
    RegistrationService registrationSRV;

    @Test
    @Disabled("Not working without authentication")
    @DisplayName("Test register user")
    void registerUserTest() {

        RegistrationRequest registrationRequest = new RegistrationRequest("Mimmo", "very-secure-password", UserRole.USER.name());
        assertDoesNotThrow(() -> registrationSRV.register(registrationRequest), "Registration should not throw an exception");
    }

}
