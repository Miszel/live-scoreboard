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

    public MatchScore createMatchScore(
            int goalsHome, int goalsAway, String homeTeam, String awayTeam, Long sequence) {
        return MatchScore.builder()
                .homeTeam(homeTeam)
                .awayTeam(awayTeam)
                .homeScore(goalsHome)
                .awayScore(goalsAway)
                .sequenceNumber(sequence)
                .build();

    }
}
