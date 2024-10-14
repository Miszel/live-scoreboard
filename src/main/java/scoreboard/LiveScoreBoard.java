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
        MatchScore matchScore = matchScoreCreator.createMatch(teamHome, teamAway);
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
