package de.cofinpro.codeshare.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Paging and sorting extending repository for DB-storage of CodeSnippets
 */
@Repository
public interface CodeSnippetRepository extends PagingAndSortingRepository<CodeSnippet, Long> {

    Optional<CodeSnippet> findByUuid(String uuid);

    Page<CodeSnippet> findAllByTimeEqualsAndViewsEquals(Pageable pageable, long timeRestriction, int viewsRestriction);

    /**
     * convenience default implementation for finding all non-secret code snippets, i.e. all snippets without time
     * or views restriction (having default value 0).
     * @param pageable the Pageable (PageRequest) object used for retrieving the result Page
     * @return result Page of the query.
     */
    default Page<CodeSnippet> findAllNonSecret(Pageable pageable) {
        return findAllByTimeEqualsAndViewsEquals(pageable, 0L, 0);
    }
}
