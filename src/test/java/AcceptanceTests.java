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
}
