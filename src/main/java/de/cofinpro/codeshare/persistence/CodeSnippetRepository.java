package de.cofinpro.codeshare.persistence;

import de.cofinpro.codeshare.domain.CodeSnippet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class CodeSnippetRepository {

    @Value("${codesharing.latest.amount:10}")
    private int amountLatest;
    private final Map<Long, CodeSnippet> codeStore = new ConcurrentHashMap<>();
    private final AtomicLong currentId = new AtomicLong(0);

    public Optional<CodeSnippet> findById(long id) {
        return Optional.ofNullable(codeStore.get(id));
    }

    public long addCode(String code) {
        long nextId = currentId.incrementAndGet();
        codeStore.put(nextId, new CodeSnippet(code, nextId));
        return nextId;
    }

    public List<CodeSnippet> findLatest() {
        return codeStore.values().stream()
                .sorted(Comparator.comparing(CodeSnippet::getDate).reversed())
                .limit(amountLatest)
                .toList();
    }
}
