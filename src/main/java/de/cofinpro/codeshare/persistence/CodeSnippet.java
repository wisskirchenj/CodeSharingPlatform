package de.cofinpro.codeshare.persistence;

import de.cofinpro.codeshare.domain.CodeSnippetRequestDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Code Snippet entity as stored in the database. The date attribute (LocalDatetime timestamp) and uuid are generated
 * in the constructor, id is an additional attribute for PK (not necessarily needed...).
 * A fromDto() convenience method maps an incoming Http request DTO to this entity.
 */
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Entity
public class CodeSnippet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String uuid;
    @Column(columnDefinition = "TEXT")
    private String code;
    private LocalDateTime date;
    private long time;
    private int views;

    public CodeSnippet(String code, long time, int views) {
        this.code = code;
        this.time = time;
        this.views = views;
        this.date = LocalDateTime.now();
        this.uuid = UUID.randomUUID().toString();
    }

    /**
     * convenience mapper from DTO to entity, which also replaces possible negative restriction fields values to 0,
     * which signifies "no restrictions".
     * @param requestDTO Http received code snippet request DTO
     * @return the entity to store.
     */
    public static CodeSnippet fromDto(CodeSnippetRequestDTO requestDTO) {
        return new CodeSnippet(requestDTO.getCode(),
                Math.max(0, requestDTO.getTime()),
                Math.max(0, requestDTO.getViews()));
    }
}
