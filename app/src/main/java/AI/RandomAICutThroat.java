package AI;

import Game.TrumpManager;
import card.Suit;
import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * The random cutthroat AI selects a random card for the cutthroat phase
 * */
public class RandomAICutThroat extends AICutThroatComponent {

    @Override
    public int calculateMove(Hand hand) {
        // Discard one of the cards in the player's hand
        if (discardPhase) {
            return discardCards(hand);
        }

        // Pick one of the 2 displayed cards
        Random random = new Random();
        return random.nextInt(2);
    }


    private int discardCards(Hand hand) {
        Map<String, Integer> suitCounts = new HashMap<>();
        suitCounts.put("S", 0);
        suitCounts.put("C", 0);
        suitCounts.put("D", 0);
        suitCounts.put("H", 0);

        String trumpSuit = TrumpManager.getInstance().getTrumpSuit();

        for (Card card : hand.getCardList()) {
            String suit = ((Suit) card.getSuit()).getSuitShortHand();
            if (!suit.equals(trumpSuit)) {
                suitCounts.put(suit, suitCounts.get(suit) + 1);
            }
        }

        String minSuit = null;
        int minCount = Integer.MAX_VALUE;

        for (Map.Entry<String, Integer> entry : suitCounts.entrySet()) {
            int count = entry.getValue();
            if (count != 0 && count < minCount) {
                minCount = count;
                minSuit = entry.getKey();
            }
        }

        int index = 0;
        for(Card card : hand.getCardList()){
            if(((Suit) card.getSuit()).getSuitShortHand().equals(minSuit)){
                return index;
            }
            index++;
        }
        return 0;
    }
}
