package de.cofinpro.codeshare.domain;

import de.cofinpro.codeshare.handler.CodeNotFoundException;
import de.cofinpro.codeshare.persistence.CodeSnippet;
import de.cofinpro.codeshare.persistence.CodeSnippetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    /**
     * central access point to code snippets (secret and non-secret) by uuid. Not only finds this method a stored
     * code snippet under the given uuid, but it also applies and updates the time and/or view restrictions
     * @param uuid given uuid to look up
     * @return a CodeSnippetResponseDto with the code snippet of given uuid or Optional.empty() if none found.
     * @throws CodeNotFoundException in case the code snippets time restriction has passed (404)
     */
    public Optional<CodeSnippetResponseDTO> findById(String uuid) {
        return snippetRepository.findByUuid(uuid).map(this::applyRestrictions);
    }

    /**
     * (if applicable) apply time and views restrictions to the given code snippet
     * and map it to a CodeSnippet response to return via Http. If time restrictions are set and the given time has
     * passed, the entry is deleted from database and exception thrown. If time has not yet passed, the remaining time
     * in seconds is mapped to the response DTO for display.
     * If view restrictions are set, the database entry is decremented by one view and deleted, if it was the last view
     * (then -1 is set as remaining views to distinguish this case from no restrictions when rendering).
     * In any case the remaining views are mapped to the response DTO for user display.
     * @param codeSnippet the code snippet as coming from repository
     * @return a CodeSnippetResponseDto with the code snippet of given uuid.
     * @throws CodeNotFoundException in case the code snippets time restriction has passed (404)
     */
    private CodeSnippetResponseDTO applyRestrictions(CodeSnippet codeSnippet) {
        long timeRemaining = codeSnippet.getTime() == 0 ? 0
                : codeSnippet.getTime() - Duration.between(codeSnippet.getDate(), LocalDateTime.now()).getSeconds();
        if (timeRemaining < 0) {
            snippetRepository.delete(codeSnippet);
            return null;
        }
        if (codeSnippet.getViews() > 0) {
            if (codeSnippet.getViews() == 1) {
                snippetRepository.delete(codeSnippet);
                codeSnippet.setViews(-1);
            } else {
                snippetRepository.save(codeSnippet.setViews(codeSnippet.getViews() - 1));
            }
        }
        return toDTO(codeSnippet.setTime(timeRemaining));
    }

    /**
     * add a new code snippet to the database and return the newly generated uuid for later retrieval.
     * @param codeRequest http code request object to store
     * @return uuid string generated during storing
     */
    public String addCode(CodeSnippetRequestDTO codeRequest) {
        return snippetRepository.save(CodeSnippet.fromDto(codeRequest)).getUuid();
    }

    /**
     * retrieves all the non-secret code snippets stored in the repository and returns a list of the
     * configurable ${amountLatest} CodeSnippets with the latest timestamp in reversed order.
     * @return the 10 latest posted non-secret code snippets
     */
    public List<CodeSnippetResponseDTO> findLatest(int page) {
        Pageable paging = PageRequest.of(page, amountLatest, Sort.Direction.DESC, "date");
        Page<CodeSnippet> pagedResult = snippetRepository.findAllNonSecret(paging);
       return pagedResult.getContent().stream().map(this::toDTO).toList();
    }

    /**
     * map entity to Http response DTO, replacing DateTime by a format string.
     * @param codeSnippet snippet to map
     * @return the mapped response DTO
     */
    private CodeSnippetResponseDTO toDTO(CodeSnippet codeSnippet) {
        return new CodeSnippetResponseDTO(codeSnippet.getCode(),
                codeSnippet.getDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")),
                codeSnippet.getTime(),
                codeSnippet.getViews());
    }
}
