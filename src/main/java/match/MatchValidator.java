package match;

public class MatchValidator {
    public static final String GOALS_CANNOT_BE_NEGATIVE = "Goals cannot be negative";

    public static void validateScore(int goalsHome, int goalsAway) {
        if (goalsHome < 0 || goalsAway < 0) {
            throw new IllegalArgumentException(GOALS_CANNOT_BE_NEGATIVE);
        }
    }

    public static void validateTeams(String teamHome, String teamAway) {
        if (teamHome == null || teamHome.trim().isEmpty()
                || teamAway == null || teamAway.trim().isEmpty()
                || teamAway.equals(teamHome)) {
            throw new IllegalArgumentException();
        }
    }
}
