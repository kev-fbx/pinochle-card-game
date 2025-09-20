package meld.combinations;

import card.Rank;
import ch.aplu.jcardgame.Card;
import meld.IMeld;
import utils.CardUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Double Run meld implementation.
 *
 * @author Kevin Tran
 */
public class DoubleRun implements IMeld {
    private final int score = 1500;

    /**
     * Checks if the hand contains a Double Ace Run in the Trump suit.
     *
     * @param hand      Cards in player's hand
     * @param trumpSuit Trump suit of the game
     * @return List of cards as String in the meld, or empty list if no meld found
     */
    @Override
    public List<String> checkForMeld(List<Card> hand, String trumpSuit) {
        List<String> meldCards = CardUtils.getCardsAsStringList(
                Rank.ACE.getRankCardValue() + trumpSuit,
                Rank.JACK.getRankCardValue() + trumpSuit,
                Rank.QUEEN.getRankCardValue() + trumpSuit,
                Rank.KING.getRankCardValue() + trumpSuit,
                Rank.TEN.getRankCardValue() + trumpSuit,
                Rank.ACE.getRankCardValue() + trumpSuit,
                Rank.JACK.getRankCardValue() + trumpSuit,
                Rank.QUEEN.getRankCardValue() + trumpSuit,
                Rank.KING.getRankCardValue() + trumpSuit,
                Rank.TEN.getRankCardValue() + trumpSuit
        );

        if (CardUtils.checkCardsInList(hand, meldCards)) {
            return meldCards;
        }
        return new ArrayList<>();
    }

    @Override
    public boolean isVanilla() {
        return false;
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
