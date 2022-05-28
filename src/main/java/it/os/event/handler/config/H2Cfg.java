package it.os.event.handler.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Getter
@Component
public class H2Cfg {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.driverClassName}")
    private String driver;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.jpa.database-platform}")
    private String dialect;

}
