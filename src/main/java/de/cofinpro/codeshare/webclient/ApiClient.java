package de.cofinpro.codeshare.webclient;

import de.cofinpro.codeshare.controller.SomeBean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class ApiClient {

    private final WebClient webClient;

    public ApiClient() {
        this.webClient = WebClient.builder().baseUrl("http://localhost:8080").build();
    }

    public Mono<SomeBean> getReactiveBean() {

        return  webClient
                .get()
                .uri("/reactive")
                .retrieve()
                .bodyToMono(SomeBean.class);
    }
}
