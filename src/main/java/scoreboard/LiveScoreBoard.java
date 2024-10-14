package scoreboard;

import dto.BoardFormatter;
import dto.ScoreBoardLineDto;
import dto.ScoreBoardLineDtoMapper;
import match.InMemoryMatchScoreRepository;
import match.MatchScoreCreator;
import match.MatchScore;
import match.MatchScoreRepository;

import java.util.List;

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
        validateTeams(teamHome, teamAway);
        MatchScore matchScore = matchScoreCreator.createMatch(teamHome.trim(), teamAway.trim());
        board.startMatch(matchScore);
    }

    /**
     * In original understanding of scoreboard summary, author expected that scoreboard needs to return formatted string,
     * which has scores in separated lines
     * @return formatted string with live scores
     */
    @Deprecated
    public String getMatches() {
        return BoardFormatter.formatScoreboard(getSummary());
    }

    public List<ScoreBoardLineDto> getSummary() {
        return board.getMatches().stream()
                .map(ScoreBoardLineDtoMapper::from)
                .toList();
    }

    public void update(String teamHome, String teamAway, int goalsHome, int goalsAway) {
        validateTeams(teamHome, teamAway);
        final String trimmedHomeTeam = teamHome.trim();
        final String trimmedAwayTeam = teamAway.trim();

        final String id = MatchScore.id(trimmedHomeTeam, trimmedAwayTeam);
        final MatchScore matchScore = MatchScore.builder()
                .homeTeam(trimmedHomeTeam)
                .awayTeam(trimmedAwayTeam)
                .homeScore(goalsHome)
                .awayScore(goalsAway)
                .sequenceNumber(this.board.getSequence(id))
                .build();

        this.board.update(matchScore);
    }

    private static void validateTeams(String teamHome, String teamAway) {
        if (teamHome == null || teamHome.trim().isEmpty()
                || teamAway == null || teamAway.trim().isEmpty()
                || teamAway.equals(teamHome)) {
            throw new IllegalArgumentException();
        }
    }

    public void finish(String teamHome, String teamAway) {
        final String id = MatchScore.id(teamHome.trim(), teamAway.trim());
        this.board.finish(id);
    }
}
