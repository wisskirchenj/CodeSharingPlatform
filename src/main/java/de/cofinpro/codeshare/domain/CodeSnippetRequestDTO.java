package de.cofinpro.codeshare.domain;

import lombok.Value;

/**
 * Immutable DTO object representing a code snippet request as send via HTML form's send button,
 * with the code text, and optional time and view restrictions.
 */
@Value
public class CodeSnippetRequestDTO {

    String code;
    long time;
    int views;
}
