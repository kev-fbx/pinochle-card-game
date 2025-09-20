package card;

import ch.aplu.jcardgame.*;

public class Table {
    private final Hand[] trickWinningHands;
    private final Hand playingArea;

    public Table(int nbPlayers, Deck deck) {
        trickWinningHands = new Hand[nbPlayers];
        playingArea = new Hand(deck);
        for (int i = 0; i < nbPlayers; i++) {
            trickWinningHands[i] = new Hand(deck);
        }
    }

    public Hand[] getTrickWinningHands() {
        return trickWinningHands;
    }
    public Hand getPlayingArea() {
        return playingArea;
    }
}
