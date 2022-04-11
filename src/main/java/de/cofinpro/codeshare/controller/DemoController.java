package de.cofinpro.codeshare.controller;

import de.cofinpro.codeshare.webclient.ApiClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public record DemoController(ApiClient webClient, SomeBean bean) {

    @GetMapping("demo")
    public SomeBean getBean() {
        return bean;
    }

    @GetMapping("react")
    public Mono<SomeBean> getClientBean() {
        return webClient.getReactiveBean();
    }
}
