package scoreboard;

import lombok.Builder;
import match.MatchScore;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Builder
public class ScoreBoard {
    private static final int INITIAL_SCORE = 0;
    public static final String CANNOT_UPDATE_MATCH_WHICH_HAVE_NOT_STARTED = "Cannot update match which have not started";
    public static final String CANNOT_START_MATCH_WHICH_IS_ALREADY_STARTED = "Cannot start match which is already started";

    private final Map<String, MatchScore> matches = new HashMap<>();

    public void startMatch(String teamHome, String teamAway) {
        if (matches.containsKey(MatchScore.id(teamHome, teamAway))) {
            throw new IllegalStateException(CANNOT_START_MATCH_WHICH_IS_ALREADY_STARTED);
        }
        final MatchScore matchScore = MatchScore.builder()
                .homeTeam(teamHome)
                .awayTeam(teamAway)
                .homeScore(INITIAL_SCORE)
                .awayScore(INITIAL_SCORE)
                .build();
        addMatchScore(matchScore);
    }

    private void addMatchScore(MatchScore matchScore) {
        matches.put(matchScore.id(), matchScore);
    }

    public String getMatches() {
        final AtomicInteger index = new AtomicInteger(1);
        return matches.values().stream()
                .sorted(Comparator.comparingInt((MatchScore m) -> m.homeScore() + m.awayScore()).reversed())
                .map(m -> index.getAndIncrement() + ". " + m)
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
        addMatchScore(matchScore);
    }
}
