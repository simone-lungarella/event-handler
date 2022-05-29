package it.os.event.handler.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Event Handler", 
        version = "1.0.0", 
        description = "Event Handler service for building sites", 
        contact = @Contact(name = "Simone Lungarella", email = "simonelungarella@gmail.com", url = "https://simone-lungarella.github.io/"),
        extensions = {
            @Extension(properties = {
                @ExtensionProperty(name = "x-summary", value = "Event Handler service for building sites")
            })
            }
        ),
    servers = {
        @Server(url = "http://localhost:8080/event-handler", description = "Development local server", extensions = {
            @Extension(properties = {
                @ExtensionProperty(name = "x-sandbox", value = "true", parseValue = true)
            })
        }),
        @Server(url = "https://dockyard-handler.herokuapp.com/", description = "Production server")    
    }
    )
public class OpenApiConfig {
    // Empty class, codes not needed
}
