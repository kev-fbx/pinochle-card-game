package AI;

import card.Suit;

import java.util.Arrays;

/**
 * The bidding component is used to generate moves for the bidding phase of the game
 * */
public abstract class AIBiddingComponent extends  AIComponent{

    protected int currBid;
    protected Suit trumpSuit = null;

    /** Get the trump suit */
    public Suit getTrumpSuit() {
        if (trumpSuit == null) {
            // Returns a random suit if the trump suit is not found
            return Arrays.stream(Suit.values()).findAny().get();
        }
        return trumpSuit;
    }

    public void setCurrBid(int currBid) {
        this.currBid = currBid;
    }
}
