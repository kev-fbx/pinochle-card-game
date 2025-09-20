package Game;

import ch.aplu.jgamegrid.*;
import card.Suit;
import utils.Constants;
import utils.Logger;

import java.awt.*;
import java.util.*;

public class TrumpManager {
    private static TrumpManager instance = null;
    private final Pinochle game;
    private String trumpSuit = null;

    private final TextActor trumpInstructionText;

    private Actor trumpSelectedIcon;

    private final Location trumpInstructionLocation = new Location(550, 80);
    private final Location trumpLocation = new Location(620, 120);

    private final Map<String, String> trumpImages;

    /** Singleton Functions */
    private TrumpManager() {
        game = Pinochle.getGameRef();

        this.trumpInstructionText = new TextActor("Trump Selection", Color.white, game.bgColor, game.smallFont);

        this.trumpImages = new HashMap<>(Map.of(
            Suit.SPADES.getSuitShortHand(), "sprites/bigspade.gif",
            Suit.CLUBS.getSuitShortHand(), "sprites/bigclub.gif",
            Suit.DIAMONDS.getSuitShortHand(), "sprites/bigdiamond.gif",
            Suit.HEARTS.getSuitShortHand(), "sprites/bigheart.gif"));
    }

    public static TrumpManager getInstance() {
        if (instance == null) {
            instance = new TrumpManager();
        }
        return instance;
    }

    public static void resetInstance() {
        instance = new TrumpManager();
    }

    /** Main Logic */
    public void askForTrumpCard(ArrayList<Player> players) {
        game.addActor(trumpInstructionText, trumpInstructionLocation);
        trumpSuit = players.get(BiddingManager.getBiddingManager().getBidWinPlayerIndex()).getTrumpCardChoice();
        updateTrumpActor();
        int[] scores = new int[Constants.NB_PLAYERS];
        scores[Constants.COMPUTER_PLAYER_INDEX] = players.get(Constants.COMPUTER_PLAYER_INDEX).getScore();
        scores[Constants.HUMAN_PLAYER_INDEX] = players.get(Constants.HUMAN_PLAYER_INDEX).getScore();
        Logger.getInstance().addTrumpInfoToLog(trumpSuit, scores);
    }

    /** Updates the displayed image to the trump suit */
    private void updateTrumpActor() {
        String trumpImage = trumpImages.get(trumpSuit);
        trumpSelectedIcon = new Actor(trumpImage);
        game.addActor(trumpSelectedIcon, trumpLocation);
    }

    /** Gets the selected trump suit */
    public String getTrumpSuit() {
        return trumpSuit;
    }

}