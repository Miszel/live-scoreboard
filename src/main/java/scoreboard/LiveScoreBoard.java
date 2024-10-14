package scoreboard;

import dto.BoardFormatter;
import dto.ScoreBoardLineDto;
import dto.ScoreBoardLineDtoMapper;
import match.InMemoryMatchScoreRepository;
import match.MatchScoreCreator;
import match.MatchScore;
import match.MatchScoreRepository;

import java.util.List;

import static match.MatchValidator.validateScore;
import static match.MatchValidator.validateTeams;

/**
 * The {@code LiveScoreBoard} class is the entry point of the library.
 * It allows users to start a new match, update the score, get a summary of ongoing matches, and finish matches.
 * <p>
 * It uses an in-memory match score repository by default, but can accept custom repositories as well.
 * <p>
 * Usage
 * example:
 * <pre>{@code
 * LiveScoreBoard scoreBoard = new LiveScoreBoard();
 * scoreBoard.startMatch("Team A", "Team B");
 * scoreBoard.update("Team A", "Team B", 2, 1);
 * String summary = scoreBoard.getMatches();
 * }</pre>
 */
public class LiveScoreBoard {
    private final Board board;
    private final MatchScoreCreator matchScoreCreator;

    /**
     * Constructor that accepts a custom repository for storing match scores.
     *
     * @param repository the repository where match scores will be stored
     */
    public LiveScoreBoard(MatchScoreRepository repository) {
        this.board = new ScoreBoard(repository);
        this.matchScoreCreator = new MatchScoreCreator();
    }

    /**
     * Default constructor that uses an in-memory repository for storing match scores.
     */
    public LiveScoreBoard() {
        this(new InMemoryMatchScoreRepository());
    }

    /**
     * Adding match to the scoreboard.
     *
     * @param teamHome the name of the home team
     * @param teamAway the name of the away team
     * @throws IllegalArgumentException if the team names are invalid (null, empty, or both teams are the same)
     * @throws IllegalStateException if the match has already started
     */
    public void startMatch(String teamHome, String teamAway) {
        validateTeams(teamHome, teamAway);
        MatchScore matchScore = matchScoreCreator.createMatch(teamHome.trim(), teamAway.trim());
        board.startMatch(matchScore);
    }

    /**
     * Retrieves a deprecated formatted string of live scores.
     * This method shows initial understanding of scoreboard summary. It is kept to make it easier to understand
     * design decisions.
     * It returns the live scoreboard in a formatted string with scores separated by lines.
     *
     * @return a formatted string representing live scores
     * @deprecated This method is deprecated in favor of {@link #getSummary()} which returns a list of DTOs.
     */
    @Deprecated
    public String getMatches() {
        return BoardFormatter.formatScoreboard(getSummary());
    }

    /**
     * Retrieves a summary of the ongoing matches in the form of a list of data transfer objects (DTOs).
     *
     * @return a list of {@link dto.ScoreBoardLineDto} representing ongoing matches
     */
    public List<ScoreBoardLineDto> getSummary() {
        return board.getMatches().stream()
                .map(ScoreBoardLineDtoMapper::from)
                .toList();
    }

    /**
     * Updates the score of an ongoing match.
     *
     * @param teamHome the name of the home team
     * @param teamAway the name of the away team
     * @param goalsHome the new score for the home team
     * @param goalsAway the new score for the away team
     * @throws IllegalArgumentException if the team names are invalid (null, empty, or both teams are the same) or the score is negative
     * @throws IllegalStateException if the match has not started yet
     */
    public void update(String teamHome, String teamAway, int goalsHome, int goalsAway) {
        validateTeams(teamHome, teamAway);
        validateScore(goalsHome, goalsAway);
        final String trimmedHomeTeam = teamHome.trim();
        final String trimmedAwayTeam = teamAway.trim();

        final String id = MatchScore.id(trimmedHomeTeam, trimmedAwayTeam);
        final MatchScore matchScore = matchScoreCreator
                .createMatchScore(goalsHome, goalsAway, trimmedHomeTeam, trimmedAwayTeam, this.board.getSequence(id));

        this.board.update(matchScore);
    }

    /**
     * Finishes a match and removes it from the scoreboard.
     *
     * @param teamHome the name of the home team
     * @param teamAway the name of the away team
     * @throws IllegalStateException if the match was not started
     */
    public void finish(String teamHome, String teamAway) {
        final String id = MatchScore.id(teamHome.trim(), teamAway.trim());
        this.board.finish(id);
    }
}
