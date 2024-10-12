import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import scoreboard.ScoreBoard;

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
        scoreBoard.update(teamHome, teamAway, 1, 0);

        String expectedBoard = "1. Sweden 1 - Norway 0";

        //when
        String result = scoreBoard.getMatches();

        //then
        Assertions.assertEquals(expectedBoard, result);
    }
}
