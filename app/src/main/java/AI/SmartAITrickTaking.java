package AI;

import Game.Pinochle;
import Game.TrumpManager;
import card.Rank;
import card.Suit;
import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import utils.CardUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * The smart trick taking AI generates the best move to make against the opponent's card
 * */
public class SmartAITrickTaking extends AITrickTakingComponent {

    ArrayList<Card> cardHistory = new ArrayList<>();

    @Override
    public int calculateMove(Hand hand) {
        List<Card> existingCards = Pinochle.getGameRef().getTable().getPlayingArea().getCardList();
        return calculateBestMove(hand.getCardList(), existingCards);
    }

    /** Calculates the best move */
    private int calculateBestMove(List<Card> hand, List<Card> playedCards) {
        // Picks a strong card if it goes first
        if (playedCards.isEmpty()) {
            return calculateFirstPlay(hand);
        }

        Card playedCard = playedCards.get(0);
        String trumpSuit = TrumpManager.getInstance().getTrumpSuit();
        int lowestValue = Integer.MAX_VALUE;
        int lowestIndex = 0;

        // Plays the lowest higher card in the same suit first
        Suit playedSuit = CardUtils.getSuitFromString(CardUtils.getCardName(playedCard));
        Rank playedRank = CardUtils.getRankFromString(CardUtils.getCardName(playedCard));
        int currIndex = 0;
        for (Card card : hand) {
            if (CardUtils.getSuitFromString(CardUtils.getCardName(card)) == playedSuit) {
                Rank rank = CardUtils.getRankFromString(CardUtils.getCardName(card));
                // Continue if the card cannot win
                if (rank.getRankCardValue() > playedRank.getRankCardValue()) {
                    continue;
                }
                // Checks whether it is the lowest value
                if (rank.getRankCardValue() < lowestValue) {
                    lowestValue = rank.getRankCardValue();
                    lowestIndex = currIndex;
                }
            }
            currIndex++;
        }
        // Adds the card to the history if it won the turn
        if (lowestValue != Integer.MAX_VALUE) {
            cardHistory.add(hand.get(lowestIndex));
            return lowestIndex;
        }


        // Plays the lowest card from the trump suit that is higher than the played card
        lowestValue = Integer.MAX_VALUE;
        currIndex = 0;
        for (Card card : hand) {
            if (CardUtils.getSuitFromString(CardUtils.getCardName(card)).getSuitShortHand().equals(trumpSuit)) {
                Rank rank = CardUtils.getRankFromString(CardUtils.getCardName(card));
                // Continue if the card cannot win
                if (rank.getRankCardValue() > playedRank.getRankCardValue()) {
                    continue;
                }
                // Checks whether it is the lowest value
                if (rank.getRankCardValue() < lowestValue) {
                    lowestValue = rank.getRankCardValue();
                    lowestIndex = currIndex;
                }
            }
            currIndex++;
        }
        // Adds the card to the history if it won the turn
        if (lowestValue != Integer.MAX_VALUE) {
            cardHistory.add(hand.get(lowestIndex));
            return lowestIndex;
        }


        // If the player cannot win with any card, plays the lowest card in a non trump suit
        lowestValue = Integer.MAX_VALUE;
        currIndex = 0;
        for (Card card : hand) {
            if (!CardUtils.getSuitFromString(CardUtils.getCardName(card)).getSuitShortHand().equals(trumpSuit)) {
                Rank rank = CardUtils.getRankFromString(CardUtils.getCardName(card));
                // Checks whether it is the lowest value
                if (rank.getRankCardValue() < lowestValue) {
                    lowestValue = rank.getRankCardValue();
                    lowestIndex = currIndex;
                }
            }
            currIndex++;
        }
        if (lowestValue != Integer.MAX_VALUE) {
            return lowestIndex;
        }


        // Finally, just plays the lowest card in a trump suit
        lowestValue = Integer.MAX_VALUE;
        currIndex = 0;
        for (Card card : hand) {
            if (CardUtils.getSuitFromString(CardUtils.getCardName(card)).getSuitShortHand().equals(trumpSuit)) {
                Rank rank = CardUtils.getRankFromString(CardUtils.getCardName(card));
                // Checks whether it is the lowest value
                if (rank.getRankCardValue() < lowestValue) {
                    lowestValue = rank.getRankCardValue();
                    lowestIndex = currIndex;
                }
            }
            currIndex++;
        }
        if (lowestValue != Integer.MAX_VALUE) {
            return lowestIndex;
        }

        return 0;
    }

    /** Selects a card that increases the chances of winning */
    public int calculateFirstPlay(List<Card> hand) {
        for (Card winningCard : cardHistory) {
            Suit winningSuit = CardUtils.getSuitFromString(CardUtils.getCardName(winningCard));
            Rank winningRank = CardUtils.getRankFromString(CardUtils.getCardName(winningCard));
            int currIndex = 0;
            // Search for a card that is better than a winning card (same suit and higher rank)
            for (Card card : hand) {
                if (CardUtils.getSuitFromString(CardUtils.getCardName(card)).equals(winningSuit)) {
                    Rank rank = CardUtils.getRankFromString(CardUtils.getCardName(card));
                    // Checks whether it is stronger than the winning card
                    if (rank.getRankCardValue() > winningRank.getRankCardValue()) {
                        return currIndex;
                    }
                }
                currIndex++;
            }
        }

        // Plays the first card if it cannot find a stronger card
        return 0;
    }
}
