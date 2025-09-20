package Game;

import java.util.*;

import card.DeckFacade;
import card.Rank;
import card.Suit;
import ch.aplu.jcardgame.*;

import utils.*;

public class DealingManager {
    private static DealingManager instance = null;

    private final DeckFacade deck = new DeckFacade(Suit.values(), Rank.values(), "cover");
    private Hand stockpile;

    /** Singleton functions */
    public static DealingManager getDealingManager() {
        if (instance == null) {
            instance = new DealingManager();
        }
        return instance;
    }

    public static void resetInstance() {
        instance = new DealingManager();
    }

    /** Game Logic */
    public void runDealingPhase(ArrayList<Player> players) {
        // Use provided input if the game is automated
        if (Pinochle.getGameRef().isAuto()) {
            autoDealCards(players, Constants.NB_PLAYERS, Constants.NB_CARDS);
        }
        // Deal cards randomly
        else {
            simpleDealCards(players, deck, Constants.NB_PLAYERS, Constants.NB_CARDS);
        }
    }

    /** Deal the cards based on the provided properties */
    public void autoDealCards(ArrayList<Player> players, int nbPlayers, int nbCardsPerPlayer) {
        stockpile = deck.getDeck().toHand(false);

        for (int i = 0; i < nbPlayers; i++) {
            String initialCardsKey = "players." + i + ".initialcards";
            String initialCardsValue = Pinochle.getGameRef().getProperties().getProperty(initialCardsKey);
            if (initialCardsValue == null) {
                continue;
            }
            String[] initialCards = initialCardsValue.split(",");
            for (String initialCard : initialCards) {
                if (initialCard.length() <= 1) {
                    continue;
                }
                Card card = CardUtils.getCardFromList(stockpile.getCardList(), initialCard);
                if (card != null) {
                    card.removeFromHand(false);
                    players.get(i).getHand().insert(card, false);
                }
            }
        }

        for (int i = 0; i < nbPlayers; i++) {
            int cardsToDealt = nbCardsPerPlayer - players.get(i).getHand().getNumberOfCards();
            for (int j = 0; j < cardsToDealt; j++) {
                if (stockpile.isEmpty()) return;
                Card dealt = CardUtils.randomCard(stockpile.getCardList());
                dealt.removeFromHand(false);
                players.get(i).getHand().insert(dealt, false);
            }
        }
    }

    /** Deal the cards randomly */
    public void simpleDealCards(ArrayList<Player> players, DeckFacade deck, int nbPlayers, int nbCardsPerPlayer) {
        Hand[] dealCards = deck.dealCards(nbPlayers, nbCardsPerPlayer);
        for (int i = 0; i < nbPlayers; i++) {
            players.get(i).setHand(dealCards[i]);
        }
        stockpile = dealCards[nbPlayers];
    }

    public Hand getStockpile() {
        return stockpile;
    }
}
