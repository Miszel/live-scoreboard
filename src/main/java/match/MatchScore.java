package match;

import lombok.Builder;

@Builder
public record MatchScore(String homeTeam, String awayTeam, int homeScore, int awayScore){

    @Override
    public String toString() {
        return String.format("%s %d - %s %d", homeTeam, homeScore, awayTeam, awayScore);
    }

    public String id() {
        return String.format("%s_%s", homeTeam, awayTeam);
    }
}
