package de.cofinpro.codeshare.domain;

import de.cofinpro.codeshare.persistence.CodeSnippet;
import de.cofinpro.codeshare.persistence.CodeSnippetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Wrapper class around the CodeSnippetRepository that maps the REST DTO-layer
 * to persistence layer CodeSnippet's. It also prepares the latest endpoint required list.
 */
@Component
public class CodeSnippetStorage {

    @Value("${codesharing.latest.amount:10}")
    private int amountLatest;
    private final CodeSnippetRepository snippetRepository;

    @Autowired
    public CodeSnippetStorage(CodeSnippetRepository snippetRepository) {
        this.snippetRepository = snippetRepository;
    }

    public Optional<CodeSnippet> findById(long id) {
        return snippetRepository.findById(id);
    }

    public long addCode(String code) {
        return snippetRepository.save(CodeSnippet.fromDto(new CodeSnippetDTO(code))).getId();
    }

    /**
     * retrueves all the repository content and returns a list of the ten CodeSnippets with the latest
     * timestamp in reversed order.
     * @return the 10 latest posted code snippets
     */
    public List<CodeSnippetDTO> findLatest() {

        List<CodeSnippetDTO> snippets = new ArrayList<>();

        Pageable paging = PageRequest.of(0, amountLatest, Sort.Direction.DESC, "date");
        Page<CodeSnippet> pagedResult = snippetRepository.findAll(paging);

        for (CodeSnippet snippet : pagedResult.getContent()) {
            snippets.add(snippet.toDTO());
        }
        return snippets;
    }
}
