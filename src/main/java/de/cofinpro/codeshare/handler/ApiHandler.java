package de.cofinpro.codeshare.handler;

import de.cofinpro.codeshare.domain.CodeSnippetRequestDTO;
import de.cofinpro.codeshare.domain.CodeSnippetResponseDTO;
import de.cofinpro.codeshare.domain.CodeSnippetStorage;
import jakarta.servlet.ServletException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.servlet.function.ServerResponse.ok;

/**
 * service layer handler class (in terms of WebMvc.fn) that handles all routed endpoint request of the application.
 */
@Service
public class ApiHandler {

    private final ParameterizedTypeReference<CodeSnippetResponseDTO> codeResponseType = new ParameterizedTypeReference<>(){};
    private final ParameterizedTypeReference<CodeSnippetRequestDTO> codeRequestType = new ParameterizedTypeReference<>(){};
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
        return ok().contentType(APPLICATION_JSON).body(findCodeByIdOrThrow(request), codeResponseType);
    }

    public ServerResponse saveNewCode(ServerRequest serverRequest) throws IOException, ServletException {
        CodeSnippetRequestDTO received = serverRequest.body(codeRequestType);
        String uuid = codeSnippetStorage.addCode(received);
        return ok().contentType(APPLICATION_JSON).body(String.format("{\"id\": \"%s\"}", uuid));
    }

    public ServerResponse getLatestCodeAsJson(ServerRequest ignoredRequest) {
        return ok().contentType(APPLICATION_JSON).body(codeSnippetStorage.findLatest());
    }

    /**
     * look up a code snippet by the uuid given as "id" path variable and return a response DTO
     * if the uuid is found and valid to display (regarding restrictions) - else throw exception.
     * @param request the http Get request
     * @return the CodeSnippetResponseDTO after retrieval and restriction application
     * @throws CodeNotFoundException in case no code is found / stored with given uuid
     */
    private CodeSnippetResponseDTO findCodeByIdOrThrow(ServerRequest request) {
        return codeSnippetStorage.findById(request.pathVariable("id"))
                .orElseThrow(CodeNotFoundException::new);
    }
}
