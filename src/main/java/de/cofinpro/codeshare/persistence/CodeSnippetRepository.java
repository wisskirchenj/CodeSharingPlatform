package de.cofinpro.codeshare.persistence;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeSnippetRepository extends PagingAndSortingRepository<CodeSnippet, Long> {
}
