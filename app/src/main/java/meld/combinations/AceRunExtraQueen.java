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
public class AceRunExtraQueen extends RunMeld {

    public AceRunExtraQueen() {
        super(190);
    }

    @Override
    public boolean isVanilla() {
        return true;
    }

    /**
     * Checks if the hand contains an Ace Run with Extra Queen in the Trump suit.
     *
     * @param hand      Cards in player's hand
     * @param trumpSuit Trump suit of the game
     * @return List of cards as String in the meld, or empty list if no meld found
     */
    @Override
    public List<String> checkForMeld(List<Card> hand, String trumpSuit) {
        List<String> meldCards = createRunCards(trumpSuit);
        meldCards.add(Rank.QUEEN.getRankCardValue() + trumpSuit);

        if (CardUtils.checkCardsInList(hand, meldCards)) {
            return meldCards;
        }
        return new ArrayList<>();
    }
}