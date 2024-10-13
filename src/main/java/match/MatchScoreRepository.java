package match;

import java.util.*;

public class MatchScoreRepository {
    private final Map<String, MatchScore> matches = new HashMap<>();

    public boolean contains(String id) {
        return matches.containsKey(id);
    }

    public void add(MatchScore matchScore) {
        matches.put(matchScore.id(), matchScore);
    }

    public MatchScore find(String id) {
        return matches.get(id);
    }

    public void delete(String id) {
        matches.remove(id);
    }

    public Collection<MatchScore> findAll() {
        return matches.values();
    }
}
