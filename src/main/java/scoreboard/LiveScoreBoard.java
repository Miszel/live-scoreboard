package scoreboard;

import match.InMemoryMatchScoreRepository;
import match.MatchScoreCreator;
import match.MatchScore;
import match.MatchScoreRepository;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class LiveScoreBoard {
    private final Board board;
    private final MatchScoreCreator matchScoreCreator;

    public LiveScoreBoard(MatchScoreRepository repository) {
        this.board = new ScoreBoard(repository);
        this.matchScoreCreator = new MatchScoreCreator();
    }

    public LiveScoreBoard() {
        this(new InMemoryMatchScoreRepository());
    }

    public void startMatch(String teamHome, String teamAway) {
        MatchScore matchScore = matchScoreCreator.createMatch(teamHome, teamAway);
        board.startMatch(matchScore);
    }

    public String getMatches() {
        final List<MatchScore> summary = board.getMatches();
        return formatScoreboard(summary);
    }

    private String formatScoreboard(List<MatchScore> sortedMatchScores) {
        final AtomicInteger index = new AtomicInteger(1);
        return sortedMatchScores.stream()
                .map(m -> String.format("%d. %s", index.getAndIncrement(), m))
                .collect(Collectors.joining("\n"));
    }

    public void update(String teamHome, String teamAway, int goalsHome, int goalsAway) {
        final String id = MatchScore.id(teamHome, teamAway);

        Long sequence = this.board.getSequence(id);
        final MatchScore matchScore = MatchScore.builder()
                .homeTeam(teamHome)
                .awayTeam(teamAway)
                .homeScore(goalsHome)
                .awayScore(goalsAway)
                .sequenceNumber(sequence)
                .build();

        this.board.update(matchScore);
    }

    public void finish(String teamHome, String teamAway) {
        final String id = MatchScore.id(teamHome, teamAway);
        this.board.finish(id);
    }
}
