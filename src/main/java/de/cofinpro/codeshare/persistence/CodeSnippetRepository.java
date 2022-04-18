package de.cofinpro.codeshare.persistence;

import de.cofinpro.codeshare.domain.CodeSnippet;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Getter
@Setter
@Repository
public class CodeSnippetRepository {

    private CodeSnippet codeSnippet;

    @Autowired
    CodeSnippetRepository(CodeSnippet initialCodeSnippet) {
        this.codeSnippet = initialCodeSnippet;
    }
}
