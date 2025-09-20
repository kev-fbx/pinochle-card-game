package meld.combinations;

import card.Rank;
import card.Suit;
import ch.aplu.jcardgame.Card;
import meld.IMeld;
import utils.CardUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Pinochle meld implementation.
 *
 * @author Kevin Tran
 */
public class PinochleMeld implements IMeld {
    private final int score = 40;

    /**
     * Checks if the hand contains a Pinochle meld.
     *
     * @param hand      Cards in player's hand
     * @param trumpSuit Trump suit of the game
     * @return List of cards as String in the meld, or empty list if no meld found
     */
    @Override
    public List<String> checkForMeld(List<Card> hand, String trumpSuit) {
        List<String> meldCards = CardUtils.getCardsAsStringList(
                Rank.JACK.getRankCardValue() + Suit.DIAMONDS.getSuitShortHand(),
                Rank.QUEEN.getRankCardValue() + Suit.SPADES.getSuitShortHand()
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
