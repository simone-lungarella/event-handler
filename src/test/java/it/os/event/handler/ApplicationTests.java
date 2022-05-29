package it.os.event.handler;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

@SpringBootTest
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

}
