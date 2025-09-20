package Game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.List;

/**
 * The Player class represents the player playing the game.
 * Each player has its own set of cards (hand) and takes turn making moves.
 * A Player can also be controlled by an AI instead of a human by initializing an AI with AIBuilder */
public abstract class Player {

    protected int playerIndex = 0;

    protected Hand hand;

    protected int score = 0;

    public Hand getHand() {
        return hand;
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    public abstract int getBiddingChoice(int currBid);

    public abstract String getTrumpCardChoice();

    public abstract Card getCutThroatChoice(List<Card> revealedCards);

    public abstract Card getCutThroatDiscardChoice(int remainingCards);

    public abstract Card getTrickTakingChoice(List<Card> placedCards);

}
