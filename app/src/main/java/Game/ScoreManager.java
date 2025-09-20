package Game;

import ch.aplu.jgamegrid.*;
import utils.Constants;
import utils.Logger;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ScoreManager {

    private static ScoreManager instance = null;
    private final Pinochle gameRef;

    // Score Actors
    private final Location textLocation = new Location(350, 450);
    private final TextActor[] scoreActors = {null, null, null, null};
    Font bigFont = new Font("Arial", Font.BOLD, 36);
    private final Location[] scoreLocations = {
            new Location(575, 675),
            new Location(25, 25),
    };

    /** Singleton Functions */
    private ScoreManager() {
        gameRef = Pinochle.getGameRef();
    }

    public static ScoreManager getScoreManager() {
        if (instance == null) {
            instance = new ScoreManager();
        }
        return instance;
    }

    public static void resetInstance() {
        instance = new ScoreManager();
    }

    /** Scoring Logic */
    public void RunScoringPhase(ArrayList<Player> players) {
        int[] scores = new int[Constants.NB_PLAYERS];
        scores[Constants.COMPUTER_PLAYER_INDEX] = players.get(Constants.COMPUTER_PLAYER_INDEX).getScore();
        scores[Constants.HUMAN_PLAYER_INDEX] = players.get(Constants.HUMAN_PLAYER_INDEX).getScore();

        for (int i = 0; i < Constants.NB_PLAYERS; i++) updateScore(players, i);
        int maxScore = 0;
        for (int i = 0; i < Constants.NB_PLAYERS; i++) if (scores[i] > maxScore) maxScore = scores[i];
        List<Integer> winners = new ArrayList<>();
        for (int i = 0; i < Constants.NB_PLAYERS; i++) if (scores[i] == maxScore) winners.add(i);
        String winText;
        if (winners.size() == 1) {
            winText = "Game over. Winner is player: " +
                    winners.iterator().next();
        } else {
            winText = "Game Over. Drawn winners are players: " +
                    String.join(", ", winners.stream().map(String::valueOf).collect(Collectors.toList()));
        }
        gameRef.addActor(new Actor("sprites/gameover.gif"), textLocation);
        gameRef.setStatusText(winText);
        gameRef.refresh();
        Logger.getInstance().addEndOfGameToLog(winners, Constants.NB_PLAYERS, gameRef.getTable().getTrickWinningHands(), scores);
    }

    /** Updates the display with the new player score */
    public void updateScore(List<Player> players, int playerIndex) {
        if (scoreActors[playerIndex] != null) {
            gameRef.removeActor(scoreActors[playerIndex]);
        }

        int displayScore = Math.max(players.get(playerIndex).getScore(), 0);
        String text = "P" + playerIndex + "[" + displayScore + "]";
        scoreActors[playerIndex] = new TextActor(text, Color.WHITE, gameRef.bgColor, bigFont);
        gameRef.addActor(scoreActors[playerIndex], scoreLocations[playerIndex]);
    }

    /** Initializes the score actors */
    public void initScores(ArrayList<Player> players) {
        for (int i = 0; i < Constants.NB_PLAYERS; i++) {
            String text = "[" + players.get(i).getScore() + "]";
            scoreActors[i] = new TextActor(text, Color.WHITE, gameRef.bgColor, bigFont);
            gameRef.addActor(scoreActors[i], scoreLocations[i]);
        }
    }
}
