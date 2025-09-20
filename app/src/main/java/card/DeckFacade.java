package card;

import ch.aplu.jcardgame.*;

public class DeckFacade {
    private final Deck deck;


    public DeckFacade() {
        this.deck = new Deck(Suit.values(), Rank.values(), "cover");
    }

    public <T extends Enum<T>, R extends Enum<R>> DeckFacade(T[] suits, R[] ranks, String cover) {
        this.deck = new Deck(suits, ranks, cover);
    }

    public Deck getDeck(){
        return this.deck;
    }

    public Hand[] dealCards(int numPlayers, int numCards) {
        return deck.dealingOut(numPlayers, numCards);
    }
}