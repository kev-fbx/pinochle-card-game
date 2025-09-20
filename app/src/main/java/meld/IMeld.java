package meld;

import ch.aplu.jcardgame.Card;

import java.util.List;

/**
 * Interface for all melds in the game. Uses the strategy pattern for easy extension.
 *
 * @author Kevin Tran
 */
public interface IMeld {
    /**
     * Checks if the hand contains a certain meld combination.
     *
     * @param hand      Cards in player's hand
     * @param trumpSuit Trump suit of the game
     * @return List of cards as String in the meld, or empty list if no meld found
     */
    List<String> checkForMeld(List<Card> hand, String trumpSuit);

    /**
     * Returns the score of the meld.
     *
     * @return Score of the meld
     */
    int getScore();

    /**
     * Checks if a meld is an addition from the base game or not.
     *
     * @return false if an addition to the game, true if vanilla meld
     */
    boolean isVanilla();
}
