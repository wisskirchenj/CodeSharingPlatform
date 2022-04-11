package de.cofinpro.codeshare;

import de.cofinpro.codeshare.configuration.CodeSharingConfiguration;
import de.cofinpro.codeshare.configuration.ReactiveConfiguration;
import de.cofinpro.codeshare.controller.SomeBean;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class CodeSharingPlatformAppTests {

	@Autowired
	private ReactiveConfiguration config;
	@Autowired
	private SomeBean bean;

	@Test
	void contextLoads() {
	}

	@Test
	void demoFunGetBeanTest() {
		WebTestClient client = WebTestClient
				.bindToRouterFunction(config.getReactiveBean(bean))
				.build();

		client.get()
				.uri("/reactive")
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(SomeBean.class)
				.isEqualTo(bean);
	}
}
