package meld;

import java.util.ArrayList;
import java.util.List;

import Game.Pinochle;
import ch.aplu.jcardgame.Card;
import utils.CardUtils;

public class MeldCalculator {
    /**
     * List of all meld strategies to calculate the score of
     */
    private static List<IMeld> meldStrategies = new ArrayList<>();
    /**
     * Singleton instance of the MeldCalculator class
     */
    private static MeldCalculator instance = null;
    private static boolean includeAdditionalMelds;

    private MeldCalculator() {
        meldStrategies = MeldFactory.getInstance().createAllMelds();
        includeAdditionalMelds = Boolean.parseBoolean(
                Pinochle.getGameRef().getProperties().getProperty("melds.additional", "false"));
    }

    /**
     * Returns the singleton instance of the MeldCalculator class.
     *
     * @return Singleton instance of the MeldCalculator class.
     */
    public static MeldCalculator getInstance() {
        if (instance == null) {
            instance = new MeldCalculator();
        }
        return instance;
    }

    public static void resetInstance() {
        instance = new MeldCalculator();
    }

    /**
     * Calculates the score of all melds in a player hand.
     * This method maintains backward compatibility.
     *
     * @param hand      Cards in player's hand
     * @param trumpSuit Trump suit of the game
     * @return Total score of all melds in the player's hand
     */
    public int calculateMeldingScore(List<Card> hand, String trumpSuit) {
        int score = 0;
        List<Card> pool = new ArrayList<>(hand);

        for (IMeld meldStrategy : meldStrategies) {
            // If we are not playing with additional melds, skip them
            if (!meldStrategy.isVanilla() && !includeAdditionalMelds) {
                continue;
            }
            // For each possible meld, we check if the current pool, initially derived from the hand,
            // has the cards to match the meld of interest. Checks for the most valuable melds first.
            List<String> matchingCardsForMeld = meldStrategy.checkForMeld(pool, trumpSuit);
            // If cards are found, we increment the score respective to the meld of interest.
            if (!matchingCardsForMeld.isEmpty()) {
                score += meldStrategy.getScore();
            }
            // Then the cards are removed from the pool so that they cannot be reused in other melds.
            pool = CardUtils.removeCardsFromList(pool, matchingCardsForMeld);
        }
        return score;
    }

}