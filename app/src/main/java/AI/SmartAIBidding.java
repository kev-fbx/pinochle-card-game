package AI;

import card.Rank;
import card.Suit;
import ch.aplu.jcardgame.Hand;
import meld.MeldCalculator;

import ch.aplu.jcardgame.Card;
import utils.CardUtils;

import java.util.List;
import java.util.Random;

/**
 * The SmartAIBidding class is an AIComponent that is applied for the bidding phase.
 * It makes smarter bidding moves based on its hand.
 * */
public class SmartAIBidding extends AIBiddingComponent {

    private static final int NUM_LIMIT = 6;

    /** Calculates the total melding score for the first bid */
    public int FirstBid(Hand hand) {
        trumpSuit = findTrumpSuit(hand.getCardList());
        return MeldCalculator.getInstance().calculateMeldingScore(hand.getCardList(), trumpSuit.getSuitShortHand());
    }

    /** Calculates whether it should pass, or bid 10/20 for subsequent bids */
    public int SubsequentBid(Hand hand) {
        int numSpades = 0;
        int numHearts = 0;
        int numDiamonds = 0;
        int numClubs  = 0;

        // Count the number of suits
        for (Card card : hand.getCardList()) {
            switch (CardUtils.getSuitFromString(CardUtils.getCardName(card))) {
                case SPADES:
                    numSpades++;
                    break;
                case HEARTS:
                    numHearts++;
                    break;
                case DIAMONDS:
                    numDiamonds++;
                    break;
                case CLUBS:
                    numClubs++;
                    break;
            }
        }

        // Calculate new bid amount
        int newBid;
        if (numSpades >= NUM_LIMIT || numHearts >= NUM_LIMIT || numDiamonds >= NUM_LIMIT || numClubs >= NUM_LIMIT) {
            newBid = 20;
        } else {
            newBid = 10;
        }

        // Get trump suit
        if (trumpSuit == null) {
            trumpSuit = findTrumpSuit(hand.getCardList());
        }

        // Calculate the value of each suit
        int highestTotalScore = 0;
        int highestNumHighValue = 0;
        for (Suit suit : Suit.values()) {
            int totalScore = 0;
            int numHighValue = 0;

            // Calculate the score and number of aces/10s/kings
            for (Card card : hand.getCardList()) {
                String cardString = CardUtils.getCardName(card);
                if (CardUtils.getSuitFromString(cardString) == suit) {
                    Rank rank = (Rank) card.getRank();
                    // Calculates the score
                    totalScore += rank.getScoreValue();
                    // Calculates the number of high value cards
                    if (rank == Rank.ACE || rank == Rank.TEN || rank == Rank.KING) {
                        numHighValue++;
                    }
                }
            }

            // Picks the highest
            if (suit == trumpSuit) {
                if (totalScore > highestTotalScore) {
                    highestTotalScore = totalScore;
                }
            }
            if (numHighValue > highestNumHighValue) {
                highestNumHighValue = numHighValue;
                if (totalScore > highestTotalScore) {
                    highestTotalScore = totalScore;
                }
            }
        }

        // Check if it should pass
        int passingScore = MeldCalculator.getInstance().calculateMeldingScore(hand.getCardList(),
                trumpSuit.getSuitShortHand());
        passingScore += highestTotalScore;
        if ((currBid + newBid) > passingScore) {
            return 0;
        }
        return newBid;
    }

    /** Finds the suit with the highest number of cards, picking one at random if 2 suits have the same
     * number of cards
     * */
    private Suit findTrumpSuit(List<Card> hand) {
        int highest = 0;
        Suit trump = Suit.CLUBS;
        for (Suit suit : Suit.values()) {
            int count = 0;
            for (Card card : hand) {
                if (CardUtils.getSuitFromString(CardUtils.getCardName(card)) == suit) {
                    count++;
                }
            }

            if (count >= highest) {
                // Picks a random suit if they have the same count
                if (count == highest) {
                    Random rand = new Random();
                    if (rand.nextInt(2) == 1) {
                        trump = suit;
                    }
                }
                // Set trump suit
                else {
                    highest = count;
                    trump = suit;
                }
            }
        }
        return trump;
    }

    @Override
    public int calculateMove(Hand hand) {
        if (currBid == 0) {
            return FirstBid(hand);
        } else {
            return SubsequentBid(hand);
        }
    }
}
