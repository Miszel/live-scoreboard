import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import scoreboard.ScoreBoard;

public class AcceptanceTests {

    @Test
    void scoreboard_should_display_unfinished_matches() {
        //given
        ScoreBoard scoreBoard = ScoreBoard.builder().build();
        String teamHome = "Sweden";
        String teamAway = "Norway";

        String expectedBoard = "1. Sweden 0 - Norway 0";

        //when
        scoreBoard.startMatch(teamHome, teamAway);

        //then
        String result = scoreBoard.getMatches();
        Assertions.assertEquals(expectedBoard, result);
    }
}
