package meld.combinations;

import card.Rank;
import card.Suit;
import ch.aplu.jcardgame.Card;
import meld.IMeld;
import utils.CardUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Common marriage meld implementation.
 *
 * @author Kevin Tran
 */
public class CommonMarriage implements IMeld {
    private final int score = 20;

    @Override
    public boolean isVanilla() {
        return false;
    }

    /**
     * Checks if the hand contains a common marriage meld.
     *
     * @param hand      Cards in player's hand
     * @param trumpSuit Trump suit of the game
     * @return List of cards as String in the meld, or empty list if no meld found
     */
    @Override
    public List<String> checkForMeld(List<Card> hand, String trumpSuit) {

        for (Suit suit : Suit.values()) {
            String suitShortHand = suit.getSuitShortHand();
            if (suitShortHand.equals(trumpSuit)) {
                continue;
            }
            List<String> cardsToCheck = CardUtils.getCardsAsStringList(
                    Rank.QUEEN.getRankCardValue() + suitShortHand,
                    Rank.KING.getRankCardValue() + suitShortHand
            );
            if (CardUtils.checkCardsInList(hand, cardsToCheck)) {
                return cardsToCheck;
            }
        }
        return new ArrayList<>();
    }

    /**
     * Returns the score of the meld.
     * @return Score of the meld
     */
    @Override
    public int getScore() {
        return score;
    }
}
