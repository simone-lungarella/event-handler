package it.os.event.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;
import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ApplicationTests {

	@Autowired
	Environment env;

	@Autowired
	EntityManager entityManager;

	@Test
	void contextLoads() {
		assertNotNull(env);
		assertNotNull(entityManager);
	}

	@Test
	void parsingJwtTest() {

		final String username = "Admin";
		final String secret = "asdfSFS34wfsdfsdfSDSD32dfsddDDerQSNCK34SOWEK5354fdgdf4";
        Key signingKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());

        String token = Jwts.builder().setSubject(("Admin")).setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + 31556952000l))
                .signWith(SignatureAlgorithm.HS256, signingKey).compact();

        Claims claims = Jwts.parser().setSigningKey(secret.getBytes(StandardCharsets.UTF_8)).parseClaimsJws(token.trim()).getBody();

        assertEquals(username, claims.getSubject());
	}
}
