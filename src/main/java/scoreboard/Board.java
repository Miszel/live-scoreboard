package scoreboard;

public interface Board {
    void startMatch(String teamHome, String teamAway);

    String getMatches();

    void update(String teamHome, String teamAway, int goalsHome, int goalsAway);

    void finish(String teamHome, String teamAway);
}
