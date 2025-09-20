package AI;

import Game.Pinochle;
import Game.TrumpManager;
import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import ch.aplu.jgamegrid.GameGrid;
import utils.CardUtils;

import java.util.List;

/**
 * The random trick taking AI picks a random card every turn
 * */
public class RandomAITrickTaking extends AITrickTakingComponent {

    @Override
    public int calculateMove(Hand hand) {
        Card selectedCard = getRandomCardForHand(hand);
        for (int i=0; i < hand.getCardList().size(); i++) {
            if (selectedCard.equals(hand.getCardList().get(i))) {
                return i;
            }
        }
        return 0;
    }

    /** Retrieves a random card in the player's hand */
    private Card getRandomCardForHand(Hand hand) {
        List<Card> existingCards = Pinochle.getGameRef().getTable().getPlayingArea().getCardList();
        if (existingCards.isEmpty()) {
            int x = Pinochle.getGameRef().getRandom().nextInt(hand.getCardList().size());
            return hand.getCardList().get(x);
        }

        GameGrid.delay(Pinochle.getGameRef().getThinkingTime());
        Card existingCard = existingCards.get(0);
        Card higherCard = CardUtils.getHigherCardFromList(existingCard, hand.getCardList());
        if (higherCard != null) {
            return higherCard;
        }

        Card trumpCard = CardUtils.getTrumpCard(hand.getCardList(), TrumpManager.getInstance().getTrumpSuit());
        if (trumpCard != null) {
            return trumpCard;
        }
        if(hand.getCardList().isEmpty()) {
            System.err.println("handIsEmpty");
        }
        int x = Pinochle.getGameRef().getRandom().nextInt(hand.getCardList().size());
        return hand.getCardList().get(x);
    }

}
