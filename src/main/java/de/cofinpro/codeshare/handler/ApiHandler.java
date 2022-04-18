package de.cofinpro.codeshare.handler;

import de.cofinpro.codeshare.domain.CodeSnippet;
import de.cofinpro.codeshare.persistence.CodeSnippetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.function.RenderingResponse;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import javax.servlet.ServletException;

import java.io.IOException;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.servlet.function.ServerResponse.ok;

@Service
public class ApiHandler {

    private final ParameterizedTypeReference<CodeSnippet> codeSnippetType = new ParameterizedTypeReference<>(){};
    private final ParameterizedTypeReference<Map.Entry<String ,String>>
            mapEntryType = new ParameterizedTypeReference<>(){};
    private final CodeSnippetRepository codeSnippetRepository;

    @Autowired
    public ApiHandler(CodeSnippetRepository codeSnippetRepository) {
        this.codeSnippetRepository = codeSnippetRepository;
    }

    public ServerResponse getCodeAsJson(ServerRequest ignoredRequest) {
        return ok().contentType(APPLICATION_JSON)
                .body(codeSnippetRepository.getCodeSnippet(), codeSnippetType);
    }

    public ServerResponse getCodeAsHtml(ServerRequest ignoredRequest) {
        return RenderingResponse.create("code")
                .modelAttribute("code", codeSnippetRepository.getCodeSnippet().getCode())
                .modelAttribute("date", codeSnippetRepository.getCodeSnippet().getDate())
                .build();
    }

    public ServerResponse getNewCode(ServerRequest ignoredRequest) {
        return RenderingResponse.create("create").build();
    }

    public ServerResponse saveNewCode(ServerRequest serverRequest) throws ServletException, IOException {
        Map.Entry<String, String> received = serverRequest.body(mapEntryType);
        codeSnippetRepository.setCodeSnippet(new CodeSnippet(received.getValue()));
        return ok().contentType(APPLICATION_JSON).body("{}");
    }
}
