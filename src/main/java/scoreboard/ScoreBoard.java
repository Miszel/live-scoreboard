package scoreboard;

import match.MatchScoreRepository;
import match.MatchScore;
import match.MatchSequence;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ScoreBoard implements Board {
    private static final int INITIAL_SCORE = 0;
    public static final String CANNOT_UPDATE_MATCH_WHICH_HAVE_NOT_STARTED = "Cannot update match which have not started";
    public static final String CANNOT_START_MATCH_WHICH_IS_ALREADY_STARTED = "Cannot start match which is already started";
    public static final String CANNOT_FINISH_MATCH_WHICH_WAS_NOT_STARTED = "Cannot finish match which was not started";
    public static final String GOALS_CANNOT_BE_NEGATIVE = "Goals cannot be negative";

    private final MatchScoreRepository repository;

    private final Comparator<MatchScore> totalScoreComparator =
            Comparator.comparingInt((MatchScore m) -> m.homeScore() + m.awayScore()).reversed();
    private final Comparator<MatchScore> startTimeComparator =
            Comparator.comparingLong(MatchScore::sequenceNumber).reversed();

    public ScoreBoard(MatchScoreRepository repository) {
        this.repository = repository;
    }

    @Override
    public void startMatch(String teamHome, String teamAway) {
        if (repository.contains(MatchScore.id(teamHome, teamAway))) {
            throw new IllegalStateException(CANNOT_START_MATCH_WHICH_IS_ALREADY_STARTED);
        }
        repository.add(initMatch(teamHome, teamAway));
    }

    private static MatchScore initMatch(String teamHome, String teamAway) {
        return MatchScore.builder()
                .homeTeam(teamHome)
                .awayTeam(teamAway)
                .homeScore(INITIAL_SCORE)
                .awayScore(INITIAL_SCORE)
                .sequenceNumber(MatchSequence.generateSequenceNumber())
                .build();
    }

    @Override
    public String getMatches() {
        final List<MatchScore> sortedMatchScores = getSortedMatchScores();
        return formatScoreboard(sortedMatchScores);
    }

    private String formatScoreboard(List<MatchScore> sortedMatchScores) {
        final AtomicInteger index = new AtomicInteger(1);
        return sortedMatchScores.stream()
                .map(m -> String.format("%d. %s", index.getAndIncrement(), m))
                .collect(Collectors.joining("\n"));
    }

    private List<MatchScore> getSortedMatchScores() {
        return repository.findAll().stream()
                .sorted(totalScoreComparator.thenComparing(startTimeComparator))
                .toList();
    }


    @Override
    public void update(String teamHome, String teamAway, int goalsHome, int goalsAway) {
        final String id = MatchScore.id(teamHome, teamAway);
        if (!repository.contains(id)) {
            throw new IllegalStateException(CANNOT_UPDATE_MATCH_WHICH_HAVE_NOT_STARTED);
        }
        if (goalsHome < 0 || goalsAway < 0) {
            throw new IllegalArgumentException(GOALS_CANNOT_BE_NEGATIVE);
        }

        final MatchScore matchScore = MatchScore.builder()
                .homeTeam(teamHome)
                .awayTeam(teamAway)
                .homeScore(goalsHome)
                .awayScore(goalsAway)
                .sequenceNumber(repository.find(id).sequenceNumber())
                .build();
        repository.add(matchScore);
    }

    @Override
    public void finish(String teamHome, String teamAway) {
        final String id = MatchScore.id(teamHome, teamAway);
        if (!repository.contains(id)) {
            throw new IllegalStateException(CANNOT_FINISH_MATCH_WHICH_WAS_NOT_STARTED);
        }
        repository.delete(id);
    }
}
