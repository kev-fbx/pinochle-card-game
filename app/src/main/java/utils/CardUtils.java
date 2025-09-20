package utils;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;

import card.*;

/**
 * Utility class for dealing with cards
 *
 * @author Kevin Tran
 */
public final class CardUtils {
    /**
     * Constant seed value
     */
    private static final int seed = 30008;
    /**
     * Random number generator
     */
    private static final Random random = new Random(seed);

    /**
     * Constructs the name of the card using its rank and suit
     *
     * @param card The card object to generate String name from
     * @return String representation of the card
     */
    public static String getCardName(Card card) {
        Suit suit = (Suit) card.getSuit();
        Rank rank = (Rank) card.getRank();
        return rank.getRankCardValue() + suit.getSuitShortHand();
    }

    /**
     * Gets the card from the list of cards that matches the specified card name
     *
     * @param cards    List of cards to search through
     * @param cardName String representation of the card name to search for
     * @return The card that matches the specified card name, else null
     */
    public static Card getCardFromList(List<Card> cards, String cardName) {
        Rank existingRank = getRankFromString(cardName);
        Suit existingSuit = getSuitFromString(cardName);
        for (Card card : cards) {
            Suit suit = (Suit) card.getSuit();
            Rank rank = (Rank) card.getRank();
            if (suit.getSuitShortHand().equals(existingSuit.getSuitShortHand())
                    && rank.getRankCardValue() == existingRank.getRankCardValue()) {
                return card;
            }
        }
        return null;
    }

    /**
     * Gets the rank from the specified card name in string format
     *
     * @param cardName String representation of the card
     * @return Rank of the card, else Ace as fallback
     */
    public static Rank getRankFromString(String cardName) {
        String rankString = cardName.substring(0, cardName.length() - 1);
        Integer rankValue = Integer.parseInt(rankString);

        for (Rank rank : Rank.values()) {
            if (rank.getShortHandValue() == rankValue) {
                return rank;
            }
        }

        return Rank.ACE;
    }

    /**
     * Gets the suit from the specified card name in string format
     *
     * @param cardName Name of card in string format
     * @return returns suit of the card, else Clubs as fallback
     */
    public static Suit getSuitFromString(String cardName) {
        String suitString = cardName.substring(cardName.length() - 1, cardName.length());

        for (Suit suit : Suit.values()) {
            if (suit.getSuitShortHand().equals(suitString)) {
                return suit;
            }
        }
        return Suit.CLUBS;
    }

    /**
     * Selects a random card from the provided list of Card objects
     *
     * @param list The list of cards to randomly select a card from
     * @return A randomly selected card from the provided list
     */
    public static Card randomCard(ArrayList<Card> list) {
        int x = random.nextInt(list.size());
        return list.get(x);
    }

    /**
     * Checks if specified cards are present in the Card List
     *
     * @param cardList     A list of Card objects
     * @param cardsToCheck A list of card names as strings
     * @return true if all cards in cardsToCheck are found in cardList, else false
     */
    public static boolean checkCardsInList(List<Card> cardList, List<String> cardsToCheck) {
        ArrayList<String> cardsToRemove = new ArrayList<>(cardsToCheck);
        for (Card card : cardList) {
            String cardName = getCardName(card);
            cardsToRemove.remove(cardName);
        }
        return cardsToRemove.isEmpty();
    }

    /**
     * Removes specified cards from the provided list of Card objects
     *
     * @param cardList      A list of Card objects
     * @param cardsToRemove A list of card names as strings
     * @return A new list of Card objects containing cards that were not removed
     */
    public static List<Card> removeCardsFromList(List<Card> cardList, List<String> cardsToRemove) {
        List<Card> newCardList = new ArrayList<>();
        List<String> newCardsToRemove = new ArrayList<>(cardsToRemove);
        for (Card card : cardList) {
            String cardName = getCardName(card);
            if (newCardsToRemove.contains(cardName)) {
                newCardsToRemove.remove(cardName);
            } else {
                newCardList.add(card);
            }
        }
        return newCardList;
    }

    /**
     * Checks if two cards have the same suit
     *
     * @param card1 First card
     * @param card2 Second card
     * @return true if cards have same suit, else false
     */
    public static boolean isSameSuit(Card card1, Card card2) {
        Suit card1Suit = (Suit) card1.getSuit();
        Suit card2Suit = (Suit) card2.getSuit();
        return card1Suit.getSuitShortHand().equals(card2Suit.getSuitShortHand());
    }

    /**
     * Checks if card1 has a higher rank than card2
     *
     * @param card1 First card
     * @param card2 Second card
     * @return true if card1 has higher rank than card2, else false
     */
    public static boolean isHigherRank(Card card1, Card card2) {
        Rank card2Rank = (Rank) card2.getRank();
        Rank card1Rank = (Rank) card1.getRank();
        return card1Rank.getRankCardValue() > card2Rank.getRankCardValue();
    }

    /**
     * Checks if a card from the list has the same suit but higher rank than the existing card exists
     *
     * @param existingCard The card to compare against to find a higher card of the same suit
     * @param cards        The list of Card objects to compare with existingCard
     * @return A card from the list that has the same suit as the existing card and a higher rank, else null
     */
    public static Card getHigherCardFromList(Card existingCard, List<Card> cards) {
        return cards.stream()
                .filter(playerCard -> {
                    return isSameSuit(existingCard, playerCard) && isHigherRank(playerCard, existingCard);
                })
                .findAny()
                .orElse(null);
    }

    /**
     * Checks if a card from the Card List matches the specified trump suit.
     *
     * @param cards     The list of Card objects
     * @param trumpSuit Shorthand representation of the trump suit
     * @return A Card object matching the trump suit, else null
     */
    public static Card getTrumpCard(List<Card> cards, String trumpSuit) {
        return cards.stream()
                .filter(playerCard -> {
                    Suit playerCardSuit = (Suit) playerCard.getSuit();
                    return playerCardSuit.getSuitShortHand().equals(trumpSuit);
                })
                .findAny()
                .orElse(null);
    }

    /**
     * Converts a list of cards into a string representation of the cards.
     *
     * @param hand List of cards to convert
     * @return List of cards in a single string, each card being separated by hyphens
     */
    public static String convertCardListoString(Hand hand) {
        StringBuilder sb = new StringBuilder();
        sb.append(hand.getCardList().stream().map(card -> {
            Rank rank = (Rank) card.getRank();
            Suit suit = (Suit) card.getSuit();
            return rank.getCardLog() + suit.getSuitShortHand();
        }).collect(Collectors.joining(",")));
        sb.append("-");
        return sb.toString();
    }

    /**
     * Creates a list of cards as strings by combining the provided suit and ranks.
     *
     * @param cards Variable number of cards as String to convert
     * @return List of cards in string format
     */
    public static List<String> getCardsAsStringList(String... cards) {
        List<String> cardList = new ArrayList<>();
        Collections.addAll(cardList, cards);
        return cardList;
    }
}
