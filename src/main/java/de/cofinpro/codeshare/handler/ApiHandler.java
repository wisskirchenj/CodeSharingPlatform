package de.cofinpro.codeshare.handler;

import de.cofinpro.codeshare.domain.CodeSnippet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.web.servlet.function.ServerResponse.ok;

@Component
public class ApiHandler {

    private final ParameterizedTypeReference<CodeSnippet> codeSnippetType = new ParameterizedTypeReference<>(){};
    private final ParameterizedTypeReference<String> stringType = new ParameterizedTypeReference<>(){};
    private final CodeSnippet codeSnippet;

    @Autowired
    public ApiHandler(CodeSnippet codeSnippet) {
        this.codeSnippet = codeSnippet;
    }

    public ServerResponse getCodeAsJson(ServerRequest ignoredRequest) {
        return ok().header("content-type", MediaType.APPLICATION_JSON_VALUE)
                .body(codeSnippet, codeSnippetType);
    }

    public ServerResponse getCodeAsHtml(ServerRequest ignoredRequest) {
        return ok().header("content-type", MediaType.TEXT_HTML_VALUE)
                .body(codeSnippet.toHtml(), stringType);
    }
}
