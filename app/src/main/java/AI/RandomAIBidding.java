package AI;

import ch.aplu.jcardgame.Hand;

import java.util.Random;

/**
 * The random bidding AI bids a random amount every turn
 * */
public class RandomAIBidding extends AIBiddingComponent {

    @Override
    public int calculateMove(Hand hand) {
        Random random = new Random();
        int randomBidBase = random.nextInt(3);
        return randomBidBase * 10;
    }
}
