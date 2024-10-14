package match;

public class MatchScoreCreator {
    private static final int INITIAL_SCORE = 0;

    public MatchScore createMatch(String teamHome, String teamAway) {
        return MatchScore.builder()
                .homeTeam(teamHome)
                .awayTeam(teamAway)
                .homeScore(INITIAL_SCORE)
                .awayScore(INITIAL_SCORE)
                .sequenceNumber(MatchSequence.generateSequenceNumber())
                .build();
    }
}
