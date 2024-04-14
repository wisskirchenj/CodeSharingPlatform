package de.cofinpro.codeshare.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 404-Error Response on invalid id given as path variable by client
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class CodeNotFoundException extends RuntimeException {

    public CodeNotFoundException() {
        super("Invalid code snippet id given!");
    }
}
