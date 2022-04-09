package de.cofinpro.codeshare.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public record DemoController(SomeBean bean) {

    @GetMapping("demo")
    public SomeBean getBean() {
        return bean;
    }
}
