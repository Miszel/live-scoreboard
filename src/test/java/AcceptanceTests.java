import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import scoreboard.LiveScoreBoard;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AcceptanceTests {
    final LiveScoreBoard scoreBoard = new LiveScoreBoard();

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

    @Test
    void cannot_finish_match_which_is_not_on_scoreboard() {
        String teamHome = "Sweden";
        String teamAway = "Norway";
        String teamHome2 = "Denmark";
        String teamAway2 = "Belgium";

        scoreBoard.startMatch(teamHome, teamAway);

        String expectedMessage = "Cannot finish match which was not started";

        Exception exception = assertThrows(IllegalStateException.class,
                () -> scoreBoard.finish(teamHome2, teamAway2));

        assertTrue(exception.getMessage().contains(expectedMessage));
        Assertions.assertEquals("1. Sweden 0 - Norway 0", scoreBoard.getMatches());
    }

    @Test
    void board_is_empty_when_no_match_is_started() {
        Assertions.assertEquals("", scoreBoard.getMatches());
    }

    @Test
    void board_does_not_accept_values_which_are_not_zero_or_positive_integer() {
        //given
        String teamHome = "Sweden";
        String teamAway = "Norway";

        scoreBoard.startMatch(teamHome, teamAway);
        String expectedBoard = "1. Sweden 0 - Norway 0";

        //when
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> scoreBoard.update(teamHome, teamAway, -1, 0));
        Exception exception2 = assertThrows(IllegalArgumentException.class,
                () -> scoreBoard.update(teamHome, teamAway, 0, -3));
        String expectedMessage = "Goals cannot be negative";

        //then
        assertTrue(exception.getMessage().contains(expectedMessage));
        assertTrue(exception2.getMessage().contains(expectedMessage));
        Assertions.assertEquals(expectedBoard, scoreBoard.getMatches());
    }

    @Test
    void combined_scenario_from_the_specification() {
        scoreBoard.startMatch("Mexico", "Canada");
        scoreBoard.startMatch("Spain", "Brazil");
        scoreBoard.startMatch("Germany", "France");
        scoreBoard.startMatch("Uruguay", "Italy");
        scoreBoard.startMatch("Argentina", "Australia");
        scoreBoard.update("Mexico", "Canada", 0, 5);
        scoreBoard.update("Spain", "Brazil", 10, 2);
        scoreBoard.update("Germany", "France", 2, 2);
        scoreBoard.update("Uruguay", "Italy", 6, 6);
        scoreBoard.update("Argentina", "Australia", 3, 1);

        String expectedBoard = """
                        1. Uruguay 6 - Italy 6
                        2. Spain 10 - Brazil 2
                        3. Mexico 0 - Canada 5
                        4. Argentina 3 - Australia 1
                        5. Germany 2 - France 2""";

        String result = scoreBoard.getMatches();

        Assertions.assertEquals(expectedBoard, result);
    }
}
