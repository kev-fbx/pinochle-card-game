package utils;

import card.Rank;
import card.Suit;
import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Singleton Logger class that logs all game actions.
 */
public final class Logger {
    private static final StringBuilder logResult = new StringBuilder();
    private static Logger instance = null;

    private Logger() {
    }

    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    public static void resetInstance() {
        instance = null;
    }

    public void addCardPlayedToLog(int player, Card card) {
        logResult.append("P" + player + "-");

        Rank cardRank = (Rank) card.getRank();
        Suit cardSuit = (Suit) card.getSuit();
        logResult.append(cardRank.getCardLog() + cardSuit.getSuitShortHand());

        logResult.append(",");
    }

    public void addBidInfoToLog(int bidWinPlayerIndex, int currentBid) {
        logResult.append("Bid:" + bidWinPlayerIndex + "-" + currentBid + "\n");
    }

    public void addTrumpInfoToLog(String trumpSuit, int[] scores) {
        logResult.append("Trump: " + trumpSuit + "\n");
        logResult.append("Melding Scores: " + scores[0] + "-" + scores[1] + "\n");
    }

    public void addRoundInfoToLog(int roundNumber) {
        logResult.append("\n");
        logResult.append("Round" + roundNumber + ":");
    }

    public void addPlayerCardsToLog(int nbPlayers, Hand[] hands) {
        logResult.append("Initial Cards:");
        for (int i = 0; i < nbPlayers; i++) {
            logResult.append("P" + i + "-");
            logResult.append(CardUtils.convertCardListoString(hands[i]));
        }
    }

    public void addEndOfGameToLog(List<Integer> winners, int nbPlayers, Hand[] trickWinningHands, int[] scores) {
        logResult.append("\n");
        logResult.append("Trick Winning: ");
        for (int i = 0; i < nbPlayers; i++) {
            logResult.append("P" + i + ":");
            logResult.append(CardUtils.convertCardListoString(trickWinningHands[i]));
        }
        logResult.append("\n");
        logResult.append("Final Score: ");
        for (int i = 0; i < scores.length; i++) {
            logResult.append(scores[i] + ",");
        }
        logResult.append("\n");
        logResult.append("Winners: " + String.join(", ", winners.stream().map(String::valueOf).collect(Collectors.toList())));
    }

    public StringBuilder getLogResult() {
        return logResult;
    }
}
