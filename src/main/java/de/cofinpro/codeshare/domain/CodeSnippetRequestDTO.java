package de.cofinpro.codeshare.domain;

/**
 * Immutable DTO object representing a code snippet request as send via HTML form's send button,
 * with the code text, and optional time and view restrictions.
 */
public record CodeSnippetRequestDTO(String code, long time, int views) {

}
