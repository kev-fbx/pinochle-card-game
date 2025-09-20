package Game;

public enum GamePhase {
    DEALING(0),
    BIDDING(1),
    MELDING(2),
    TRICK_TAKING(3),
    SCORING(4),
    CUT_THROAT(5);


    private final int gamePhase;

    GamePhase(int gamePhase) {
        this.gamePhase = gamePhase;
    }

    public int getGamePhase() {
        return gamePhase;
    }
}
