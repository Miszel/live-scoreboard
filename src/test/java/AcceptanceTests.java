import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import scoreboard.ScoreBoard;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AcceptanceTests {
    ScoreBoard scoreBoard = ScoreBoard.builder().build();

    @Test
    void scoreboard_should_display_unfinished_matches() {
        //given
        String teamHome = "Sweden";
        String teamAway = "Norway";

        scoreBoard.startMatch(teamHome, teamAway);

        String expectedBoard = "1. Sweden 0 - Norway 0";

        //when
        String result = scoreBoard.getMatches();

        //then
        Assertions.assertEquals(expectedBoard, result);
    }

    @Test
    void existing_match_can_be_updated() {
        //given
        String teamHome = "Sweden";
        String teamAway = "Norway";

        scoreBoard.startMatch(teamHome, teamAway);
        String expectedBoard = "1. Sweden 1 - Norway 0";

        //when
        scoreBoard.update(teamHome, teamAway, 1, 0);

        //then
        String result = scoreBoard.getMatches();
        Assertions.assertEquals(expectedBoard, result);
    }

    @Test
    void non_existing_match_cannot_be_updated() {
        //given
        String teamHome = "Sweden";
        String teamAway = "Norway";
        String expectedMessage = "Cannot update match which have not started";
        String expectedBoard = "1. Sweden 0 - Norway 0";

        scoreBoard.startMatch(teamHome, teamAway);
        //when
        Exception exception = assertThrows(IllegalStateException.class,
                () -> scoreBoard.update("Denmark", "Belgium", 0, 1));

        //then
        assertTrue(exception.getMessage().contains(expectedMessage));
        Assertions.assertEquals(expectedBoard, scoreBoard.getMatches());
    }

    @Test
    void match_cannot_be_started_multiple_times() {
        String teamHome = "Sweden";
        String teamAway = "Norway";

        String expectedMessage = "Cannot start match which is already started";
        String expectedBoard = "1. Sweden 0 - Norway 0";

        scoreBoard.startMatch(teamHome, teamAway);
        Exception exception = assertThrows(IllegalStateException.class,
                () -> scoreBoard.startMatch(teamHome, teamAway));

        assertTrue(exception.getMessage().contains(expectedMessage));
        Assertions.assertEquals(expectedBoard, scoreBoard.getMatches());
    }

    @Test
    void matches_should_be_sorted_by_total_goals() {
        String teamHome = "Sweden";
        String teamAway = "Norway";
        String teamHome2 = "Denmark";
        String teamAway2 = "Belgium";

        scoreBoard.startMatch(teamHome, teamAway);
        scoreBoard.startMatch(teamHome2, teamAway2);
        scoreBoard.update(teamHome, teamAway, 1, 0);

        String expectedBoard = "1. Sweden 1 - Norway 0\n2. Denmark 0 - Belgium 0";

        String result = scoreBoard.getMatches();

        Assertions.assertEquals(expectedBoard, result);
    }

    @Test
    void matches_with_the_same_total_goals_are_sorted_by_start_time() {
        String teamHome = "Sweden";
        String teamAway = "Norway";
        String teamHome2 = "Denmark";
        String teamAway2 = "Belgium";

        scoreBoard.startMatch(teamHome, teamAway);
        scoreBoard.startMatch(teamHome2, teamAway2);

        String expectedBoard = "1. Denmark 0 - Belgium 0\n2. Sweden 0 - Norway 0";

        String result = scoreBoard.getMatches();

        Assertions.assertEquals(expectedBoard, result);
    }

    @Test
    void finished_match_is_removed_from_the_board() {
        String teamHome = "Sweden";
        String teamAway = "Norway";
        String teamHome2 = "Denmark";
        String teamAway2 = "Belgium";

        scoreBoard.startMatch(teamHome, teamAway);
        scoreBoard.startMatch(teamHome2, teamAway2);

        String expectedBoard = "1. Sweden 0 - Norway 0";

        scoreBoard.finish(teamHome2, teamAway2);

        String result = scoreBoard.getMatches();
        Assertions.assertEquals(expectedBoard, result);
    }

}
