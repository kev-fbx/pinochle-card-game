package meld.combinations;

import card.Rank;
import ch.aplu.jcardgame.Card;
import utils.CardUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Ace Run Extra King meld implementation.
 *
 * @author Kevin Tran
 */
public class AceRunExtraKing extends RunMeld {

    public AceRunExtraKing() {
        super(190);
    }

    @Override
    public boolean isVanilla() {
        return true;
    }

    /**
     * Checks if the hand contains an Ace Run with Extra King in the Trump suit.
     *
     * @param hand      Cards in player's hand
     * @param trumpSuit Trump suit of the game
     * @return List of cards as String in the meld, or empty list if no meld found
     */
    @Override
    public List<String> checkForMeld(List<Card> hand, String trumpSuit) {
        List<String> cardsToCheck = createRunCards(trumpSuit);
        cardsToCheck.add(Rank.KING.getRankCardValue() + trumpSuit);

        if (CardUtils.checkCardsInList(hand, cardsToCheck)) {
            return cardsToCheck;
        }
        return new ArrayList<>();
    }
}