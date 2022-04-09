package de.cofinpro.codeshare;

import de.cofinpro.codeshare.controller.DemoController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class CodeSharingPlatformAppTests {

	@Autowired
	private DemoController demoController;

	@Test
	void contextLoads() {
		assertNotNull(demoController);
	}
}
