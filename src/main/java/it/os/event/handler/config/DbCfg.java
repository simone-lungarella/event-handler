package it.os.event.handler.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Getter
@Component
public class DbCfg {

    @Value("${spring.datasource.url}")
    private String url;
 
    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;
    
    @Value("${spring.datasource.driver}")
    private String driver;

    
}

