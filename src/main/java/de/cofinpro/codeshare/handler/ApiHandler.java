package de.cofinpro.codeshare.handler;

import de.cofinpro.codeshare.domain.CodeSnippetDTO;
import de.cofinpro.codeshare.domain.CodeSnippetStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import javax.servlet.ServletException;

import java.io.IOException;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.servlet.function.ServerResponse.ok;

@Service
public class ApiHandler {

    private final ParameterizedTypeReference<CodeSnippetDTO> codeSnippetType = new ParameterizedTypeReference<>(){};
    private final ParameterizedTypeReference<Map.Entry<String, String>> mapEntryType = new ParameterizedTypeReference<>(){};
    private final CodeSnippetStorage codeSnippetStorage;

    @Autowired
    public ApiHandler(CodeSnippetStorage codeSnippetStorage) {
        this.codeSnippetStorage = codeSnippetStorage;
    }

    public ServerResponse getCodeAsHtml(ServerRequest request) {
        return ok().render("code", findCodeByIdOrThrow(request));
    }

    public ServerResponse getNewCode(ServerRequest ignoredRequest) {
        return ok().render("create");
    }

    public ServerResponse getLatestCodeAsHtml(ServerRequest ignoredRequest) {
        return ok().render("latest", codeSnippetStorage.findLatest());
    }

    public ServerResponse getCodeAsJson(ServerRequest request) {
        return ok().contentType(APPLICATION_JSON).body(findCodeByIdOrThrow(request), codeSnippetType);
    }

    public ServerResponse saveNewCode(ServerRequest serverRequest) throws ServletException, IOException {
        Map.Entry<String, String> received = serverRequest.body(mapEntryType);
        long id = codeSnippetStorage.addCode(received.getValue());
        return ok().contentType(APPLICATION_JSON).body(String.format("{\"id\": \"%d\"}", id));
    }

    public ServerResponse getLatestCodeAsJson(ServerRequest ignoredRequest) {
        return ok().contentType(APPLICATION_JSON).body(codeSnippetStorage.findLatest());
    }

    private CodeSnippetDTO findCodeByIdOrThrow(ServerRequest request) {
        return codeSnippetStorage
                .findById(Long.parseLong(request.pathVariable("id")))
                .orElseThrow(CodeNotFoundException::new)
                .toDTO();
    }
}
