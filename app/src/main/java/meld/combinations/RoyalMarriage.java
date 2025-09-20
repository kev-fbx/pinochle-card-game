package meld.combinations;

import java.util.ArrayList;
import java.util.List;

import ch.aplu.jcardgame.Card;
import card.*;
import meld.IMeld;
import utils.CardUtils;

/**
 * Royal Marriage meld implementation.
 *
 * @author Kevin Tran
 */
public class RoyalMarriage implements IMeld {
    private final int score = 40;

    /**
     * Checks if the hand contains a King and Queen in the Trump suit.
     *
     * @param hand      Cards in player's hand
     * @param trumpSuit Trump suit of the game
     * @return List of cards as String in the meld, or empty list if no meld found
     */
    @Override
    public List<String> checkForMeld(List<Card> hand, String trumpSuit) {
        List<String> cardsToCheck = CardUtils.getCardsAsStringList(
                Rank.QUEEN.getRankCardValue() + trumpSuit,
                Rank.KING.getRankCardValue() + trumpSuit
        );

        if (CardUtils.checkCardsInList(hand, cardsToCheck)) {
            return cardsToCheck;
        }
        return new ArrayList<>();
    }

    @Override
    public boolean isVanilla() {
        return true;
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
