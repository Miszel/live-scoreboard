package scoreboard;

import dto.ScoreBoardLineDto;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LiveScoreBoardTest {
    LiveScoreBoard scoreBoard = new LiveScoreBoard();

    @Test
    void shouldThrowExceptionWhenHomeTeamHasNoCharactersOnMatchStart() {
        assertThrows(IllegalArgumentException.class,
                () -> scoreBoard.startMatch(" ", "Belgium"));
    }

    @Test
    void shouldThrowExceptionWhenAwayTeamHasNoCharactersOnMatchStart() {
        assertThrows(IllegalArgumentException.class,
                () -> scoreBoard.startMatch("Denmark", null));
    }

    @Test
    void shouldThrowExceptionWhenHomeTeamHasNoCharactersOnMatchUpdate() {
        assertThrows(IllegalArgumentException.class,
                () -> scoreBoard.update(" ", "Belgium", 1, 1));
    }

    @Test
    void shouldThrowExceptionWhenAwayTeamHasNoCharactersOnMatchUpdate() {
        assertThrows(IllegalArgumentException.class,
                () -> scoreBoard.update("Denmark", null, 1, 1));
    }

    @Test
    void shouldThrowExceptionWhenHomeTeamIsTheSameAsAwayTeam() {
        assertThrows(IllegalArgumentException.class,
                () -> scoreBoard.startMatch("Austria", "Austria"));
    }

    @Test
    void shouldHomeTeamNamesBeTrimmedOnMatchCreation() {
        scoreBoard.startMatch("     Slovakia       ", "     Finland ");

        final List<ScoreBoardLineDto> summary = scoreBoard.getSummary();

        assertEquals(1, summary.size());
        assertEquals("Slovakia", summary.getFirst().homeTeam());
        assertEquals("Finland", summary.getFirst().awayTeam());
    }

    @Test
    void shouldHomeTeamNamesBeTrimmedOnUpdate() {
        scoreBoard.startMatch("Slovakia", "Finland");

        scoreBoard.update("     Slovakia       ", "     Finland ", 11, 2);

        final List<ScoreBoardLineDto> summary = scoreBoard.getSummary();
        assertEquals(1, summary.size());
        assertEquals("Slovakia", summary.getFirst().homeTeam());
        assertEquals("Finland", summary.getFirst().awayTeam());
    }

    @Test
    void shouldHomeTeamNamesBeTrimmedOnMatchFinish() {
        scoreBoard.startMatch("Slovakia", "Finland");

        scoreBoard.finish("     Slovakia    ", "     Finland ");

        final List<ScoreBoardLineDto> summary = scoreBoard.getSummary();
        assertEquals(0, summary.size());
    }
}
