package meld.combinations;

import card.Rank;
import card.Suit;
import ch.aplu.jcardgame.Card;
import meld.IMeld;
import utils.CardUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Aces Around meld implementation.
 *
 * @author Kevin Tran
 */
public class AcesAround implements IMeld {
    private final int score = 100;

    @Override
    public boolean isVanilla() {
        return false;
    }

    /**
     * Checks if the hand contains Aces of each suit.
     *
     * @param hand      Cards in player's hand
     * @param trumpSuit Trump suit of the game
     * @return List of cards as String in the meld, or empty list if no meld found
     */
    @Override
    public List<String> checkForMeld(List<Card> hand, String trumpSuit) {
        List<String> meldCards = CardUtils.getCardsAsStringList(
                Rank.ACE.getRankCardValue() + Suit.SPADES.getSuitShortHand(),
                Rank.ACE.getRankCardValue() + Suit.DIAMONDS.getSuitShortHand(),
                Rank.ACE.getRankCardValue() + Suit.CLUBS.getSuitShortHand(),
                Rank.ACE.getRankCardValue() + Suit.HEARTS.getSuitShortHand()
        );

        if (CardUtils.checkCardsInList(hand, meldCards)) {
            return meldCards;
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
