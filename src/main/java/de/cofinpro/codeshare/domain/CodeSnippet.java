package de.cofinpro.codeshare.domain;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class CodeSnippet {

    String code;

    public String toHtml() {
        return "<html>\n<head>\n    <title>Code</title>\n</head>\n<body>\n    <pre>\n" + code
                + "</pre>\n</body>\n</html>";
    }
}
