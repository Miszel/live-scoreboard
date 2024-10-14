package scoreboard;

import match.MatchScore;

import java.util.List;

public interface Board {
    void startMatch(MatchScore matchScore);

    List<MatchScore> getMatches();

    void update(MatchScore matchScore);

    void finish(String id);

    Long getSequence(String id);
}
