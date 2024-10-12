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
    public static final String CANNOT_UPDATE_MATCH_WHICH_HAVE_NOT_STARTED = "Cannot update match which have not started";
    private final Map<String, MatchScore> matches = new HashMap<>();

    public void startMatch(String teamHome, String teamAway) {
        final MatchScore matchScore = MatchScore.builder()
                .homeTeam(teamHome)
                .awayTeam(teamAway)
                .homeScore(INITIAL_SCORE)
                .awayScore(INITIAL_SCORE)
                .build();
        matches.put(matchScore.id(), matchScore);
    }

    public String getMatches() {
        final AtomicInteger index = new AtomicInteger(1);
        return matches.values().stream()
                .map(m -> index.getAndIncrement() + ". " + m.toString())
                .collect(Collectors.joining("\n"));
    }


    public void update(String teamHome, String teamAway, int goalsHome, int goalsAway) {
        if (!matches.containsKey(MatchScore.id(teamHome, teamAway))) {
            throw new IllegalStateException(CANNOT_UPDATE_MATCH_WHICH_HAVE_NOT_STARTED);
        }
        final MatchScore matchScore = MatchScore.builder()
                .homeTeam(teamHome)
                .awayTeam(teamAway)
                .homeScore(goalsHome)
                .awayScore(goalsAway)
                .build();
        matches.put(matchScore.id(), matchScore);
    }
}
