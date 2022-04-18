package de.cofinpro.codeshare.domain;

import lombok.Value;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Value
public class CodeSnippet {

    String code;
    String date;

    public CodeSnippet(String code) {
        this.code = code;
        this.date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
    }
}
