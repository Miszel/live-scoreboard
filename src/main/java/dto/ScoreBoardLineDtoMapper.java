package dto;

import match.MatchScore;

public class ScoreBoardLineDtoMapper {
    public static ScoreBoardLineDto from(MatchScore matchScore) {
        return new ScoreBoardLineDto(
                matchScore.homeTeam(),
                matchScore.awayTeam(),
                matchScore.homeScore(),
                matchScore.awayScore());
    }
}
