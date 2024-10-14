package dto;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class BoardFormatter {

    public static String formatScoreboard(List<ScoreBoardLineDto> sortedMatchScores) {
        final AtomicInteger index = new AtomicInteger(1);
        return sortedMatchScores.stream()
                .map(m -> String.format("%d. %s", index.getAndIncrement(), formatLine(m)))
                .collect(Collectors.joining("\n"));
    }

    public static String formatLine(ScoreBoardLineDto line) {
        return String.format("%s %d - %s %d", line.homeTeam(), line.homeScore(), line.awayTeam(), line.awayScore());
    }
}
