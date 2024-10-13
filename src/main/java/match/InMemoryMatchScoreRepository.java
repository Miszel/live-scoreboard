package match;

import java.util.*;

public final class InMemoryMatchScoreRepository implements MatchScoreRepository {
    private final Map<String, MatchScore> matches = new HashMap<>();

    @Override
    public boolean contains(String id) {
        return matches.containsKey(id);
    }

    @Override
    public void add(MatchScore matchScore) {
        matches.put(matchScore.id(), matchScore);
    }

    @Override
    public MatchScore find(String id) {
        return matches.get(id);
    }

    @Override
    public void delete(String id) {
        matches.remove(id);
    }

    @Override
    public Collection<MatchScore> findAll() {
        return matches.values();
    }
}
