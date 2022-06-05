package it.os.event.handler.controller.impl;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import it.os.event.handler.entity.AuthInfo;
import it.os.event.handler.entity.UserETY;
import it.os.event.handler.repository.IUserRepo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/")
public class LoginCTL {

    @Autowired
    private IUserRepo userRepo;

    @Value("${jwt-token.duration-time}")
    private Long jwtTokenTime;

    @Value("${jwt-token.secret}")
    private String jwtSecret;

    /*
     * The authentication manager.
     */
    @Autowired
    AuthenticationManager authenticationManager;

    @GetMapping("login")
    AuthInfo login(final String username, final String password) {

        AuthInfo authInfo = new AuthInfo();
        
        log.info("Retrieving user with username {}", username);
        Optional<UserETY> user = userRepo.findByUsername(username);

        // Checking if user exists
        if (user.isPresent()) {

            log.info("User found");

            // Validating user password
            boolean isValidPassword = BCrypt.checkpw(password, user.get().getPassword());
            if (isValidPassword) {
            
                log.info("Password validated, proceeding to generate a jwt token");
                
                // Generate jwt token from username and password
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(username, password));

                SecurityContextHolder.getContext().setAuthentication(authentication);
                authInfo.setJwtToken(generateJwtToken(authentication));
                authInfo.setUsername(username);
                authInfo.setRoleList(user.get().getRole().name());
            }
        }

        log.info("Token generated");
        return authInfo;
    }

    private String generateJwtToken(Authentication authentication) {
        
        UserETY userAuth = (UserETY) authentication.getPrincipal();
        
        return Jwts.builder()
                .setSubject((userAuth.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtTokenTime))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }
}
