package dto;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BoardFormatterTest {

    @Test
    public void shouldFormatLine() {
        ScoreBoardLineDto lineDto = new ScoreBoardLineDto("Sweden", "Norway", 3, 2);

        String formattedLine = BoardFormatter.formatLine(lineDto);

        assertEquals("Sweden 3 - Norway 2", formattedLine);
    }

    @Test
    public void shouldBoardLineHaveIndex() {
        // Given
        List<ScoreBoardLineDto> scoreboard = List.of(
                new ScoreBoardLineDto("Sweden", "Norway", 3, 2)
        );

        // When
        String formattedScoreboard = BoardFormatter.formatScoreboard(scoreboard);

        // Then
        String expected = "1. Sweden 3 - Norway 2";
        assertEquals(expected, formattedScoreboard);
    }

    @Test
    public void shouldMatchesBeInSeparateLines() {
        // Given
        List<ScoreBoardLineDto> scoreboard = List.of(
                new ScoreBoardLineDto("Sweden", "Norway", 3, 2),
                new ScoreBoardLineDto("Turkey", "Greece", 1, 0),
                new ScoreBoardLineDto("Albania", "Costa Rica", 2, 2)
        );

        // When
        String formattedScoreboard = BoardFormatter.formatScoreboard(scoreboard);

        // Then
        String expected = """
                1. Sweden 3 - Norway 2
                2. Turkey 1 - Greece 0
                3. Albania 2 - Costa Rica 2""";
        assertEquals(expected, formattedScoreboard);
    }

    @Test
    public void shouldScoreboardBeEmptyWhenNoMatches() {
        // Given
        List<ScoreBoardLineDto> scoreboard = List.of();

        // When
        String formattedScoreboard = BoardFormatter.formatScoreboard(scoreboard);

        // Then
        assertEquals("", formattedScoreboard);
    }
}
