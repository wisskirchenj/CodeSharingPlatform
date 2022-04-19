package de.cofinpro.codeshare.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Value;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Value
public class CodeSnippet {

    String code;
    String date;
    @JsonIgnore
    Long id;

    public CodeSnippet(String code, long id) {
        this.code = code;
        this.id = id;
        this.date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
    }
}
