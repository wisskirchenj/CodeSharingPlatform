package de.cofinpro.codeshare.domain;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Immutable DTO object representing a code snippet with the code text and a timestamp of creation.
 */
@Value
@AllArgsConstructor
public class CodeSnippetDTO {

    String code;
    String date;

    public CodeSnippetDTO(String code) {
        this.code = code;
        this.date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
    }
}
