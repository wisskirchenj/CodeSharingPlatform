package de.cofinpro.codeshare.persistence;

import de.cofinpro.codeshare.domain.CodeSnippetDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class CodeSnippet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "TEXT")
    private String code;
    private String date;

    public CodeSnippet(String code, String date) {
        this.code = code;
        this.date = date;
    }

    public static CodeSnippet fromDto(CodeSnippetDTO codeSnippetDTO) {
        return new CodeSnippet(codeSnippetDTO.getCode(), codeSnippetDTO.getDate());
    }

    public CodeSnippetDTO toDTO() {
        return new CodeSnippetDTO(code, date);
    }
}
