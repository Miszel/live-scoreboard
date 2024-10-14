package match;

import lombok.Builder;

@Builder
public record MatchScore(String homeTeam, String awayTeam, int homeScore, int awayScore, Long sequenceNumber){

    public String id() {
        return formatId(homeTeam, awayTeam);
    }

    public static String id(String homeTeam, String awayTeam) {
        return formatId(homeTeam, awayTeam);
    }

    private static String formatId(String homeTeam, String awayTeam) {
        return String.format("%s_%s", homeTeam, awayTeam);
    }
}
