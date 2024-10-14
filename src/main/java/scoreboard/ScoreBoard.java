package scoreboard;

import match.MatchScore;
import match.MatchScoreRepository;

import java.util.*;

public class ScoreBoard implements Board {
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
    public void startMatch(MatchScore matchScore) {
        if (repository.contains(matchScore.id())) {
            throw new IllegalStateException(CANNOT_START_MATCH_WHICH_IS_ALREADY_STARTED);
        }
        repository.add(matchScore);
    }

    @Override
    public List<MatchScore> getMatches() {
        return getSortedMatchScores();
    }

    private List<MatchScore> getSortedMatchScores() {
        return repository.findAll().stream()
                .sorted(totalScoreComparator.thenComparing(startTimeComparator))
                .toList();
    }


    @Override
    public void update(MatchScore matchScore) {
        if (matchScore.sequenceNumber() == null) {
            throw new IllegalStateException(CANNOT_UPDATE_MATCH_WHICH_HAVE_NOT_STARTED);
        }
        if (matchScore.homeScore() < 0 || matchScore.awayScore() < 0) {
            throw new IllegalArgumentException(GOALS_CANNOT_BE_NEGATIVE);
        }
        repository.add(matchScore);
    }

    @Override
    public void finish(String id) {
        if (!repository.contains(id)) {
            throw new IllegalStateException(CANNOT_FINISH_MATCH_WHICH_WAS_NOT_STARTED);
        }
        repository.delete(id);
    }

    @Override
    public Long getSequence(String id) {
        final MatchScore matchScore = repository.find(id);
        return (matchScore != null) ? matchScore.sequenceNumber() : null;
    }
}
