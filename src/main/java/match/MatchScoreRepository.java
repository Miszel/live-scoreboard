package match;

import java.util.Collection;

public interface MatchScoreRepository {
    boolean contains(String id);

    void add(MatchScore matchScore);

    MatchScore find(String id);

    void delete(String id);

    Collection<MatchScore> findAll();
}
