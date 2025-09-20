package meld.combinations;

import card.Rank;
import ch.aplu.jcardgame.Card;
import utils.CardUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Ace Run meld implementation.
 *
 * @author Kevin Tran
 */
public class AceRunRoyalMarriage extends RunMeld {

    public AceRunRoyalMarriage() {
        super(230);
    }

    @Override
    public boolean isVanilla() {
        return false;
    }

    /**
     * Checks if the hand contains an Ace Run in the Trump suit with a Royal Marriage.
     *
     * @param hand      Cards in player's hand
     * @param trumpSuit Trump suit of the game
     * @return List of cards as String in the meld, or empty list if no meld found
     */
    @Override
    public List<String> checkForMeld(List<Card> hand, String trumpSuit) {
        List<String> meldCards = createRunCards(trumpSuit);
        meldCards.add(Rank.KING.getRankCardValue() + trumpSuit);
        meldCards.add(Rank.QUEEN.getRankCardValue() + trumpSuit);

        if (CardUtils.checkCardsInList(hand, meldCards)) {
            return meldCards;
        }
        return new ArrayList<>();
    }
}