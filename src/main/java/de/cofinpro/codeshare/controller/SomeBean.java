package de.cofinpro.codeshare.controller;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class SomeBean {

    private final String name = "hello world!";
    private final int number = 42;

    public SomeBean() {
    }

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

}
