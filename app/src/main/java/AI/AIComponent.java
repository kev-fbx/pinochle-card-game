package AI;

import ch.aplu.jcardgame.Hand;

/**
 * The AIComponent class can be subclassed to represent the different phases of the game
 * It is used to generate logic for the AI
 * */
public abstract class AIComponent {
    /** Calculates the move to make for the current turn */
    public abstract int calculateMove(Hand hand);
}
