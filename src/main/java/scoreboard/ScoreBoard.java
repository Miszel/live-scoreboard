package scoreboard;

import lombok.Builder;
import match.MatchScore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Builder
public class ScoreBoard {
    private static final int INITIAL_SCORE = 0;
    private final Map<String, MatchScore> matches = new HashMap<>();

    public void startMatch(String teamHome, String teamAway) {
        MatchScore matchScore = MatchScore.builder()
                .homeTeam(teamHome)
                .awayTeam(teamAway)
                .homeScore(INITIAL_SCORE)
                .awayScore(INITIAL_SCORE)
                .build();
        matches.put(matchScore.id(), matchScore);
    }

    public String getMatches() {
        AtomicInteger index = new AtomicInteger(1);
        return matches.values().stream()
                .map(m -> index.getAndIncrement() + ". " + m.toString())
                .collect(Collectors.joining("\n"));
    }


}
