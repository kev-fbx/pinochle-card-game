package AI;

import Game.Pinochle;
import Game.Player;
import ch.aplu.jcardgame.Card;

import java.util.List;

/**
 * The AI player generates moves based on the selected components
 * It is a subclass of the Player class, which provides input to the game
 * */
public class AIPlayer extends Player {

    AIBiddingComponent biddingComponent;
    AITrickTakingComponent trickTakingComponent;
    AICutThroatComponent cutThroatComponent;

    AIPlayer(AIBiddingComponent biddingComponent, AITrickTakingComponent trickTakingComponent,
             AICutThroatComponent cutThroatComponent) {
        this.biddingComponent = biddingComponent;
        this.trickTakingComponent = trickTakingComponent;
        this.cutThroatComponent = cutThroatComponent;

    }

    /**
     * Generates a move for the bidding phase of the game
     * */
    @Override
    public int getBiddingChoice(int currBid) {
        biddingComponent.setCurrBid(currBid);
        return biddingComponent.calculateMove(getHand());
    }

    /**
     * Returns the trump suit found from the bidding component
     */
    @Override
    public String getTrumpCardChoice() {
        return  biddingComponent.getTrumpSuit().getSuitShortHand();
    }

    /**
     * Generates a move for the trick taking phase of the game
     */
    @Override
    public Card getTrickTakingChoice(List<Card> placedCards) {
        Pinochle.getGameRef().setStatus("Player " + playerIndex + " thinking...");
        int index = trickTakingComponent.calculateMove(hand);
        return hand.getCardList().get(index);
    }

    /**
     * Generates a move for the cutthroat phase of the game
     * */
    @Override
    public Card getCutThroatChoice(List<Card> revealedCards) {
        cutThroatComponent.setDiscardPhase(false);
        int index = cutThroatComponent.calculateMove(hand);
        return revealedCards.get(index);
    }

    /**
     * Generates a move for discarding a cutthroat card
     * */
    @Override
    public Card getCutThroatDiscardChoice(int remainingCards) {
        cutThroatComponent.setDiscardPhase(true);
        int index = cutThroatComponent.calculateMove(hand);
        return hand.get(index);
    }
}
