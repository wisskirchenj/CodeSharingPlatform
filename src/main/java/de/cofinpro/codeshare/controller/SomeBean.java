package de.cofinpro.codeshare.controller;

import org.springframework.stereotype.Component;

@Component
public class SomeBean {

    private static final String NAME = "hello world!";
    private static final int NUMBER = 42;

    public int getNumber() {
        return NUMBER;
    }

    public String getName() {
        return NAME;
    }

}
