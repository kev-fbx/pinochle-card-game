package meld.combinations;

import card.Rank;
import meld.IMeld;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for all Run-based melds. Allows easier extension of Run-based melds using Template Method Pattern.
 *
 * @author Kevin Tran
 */
public abstract class RunMeld implements IMeld {
    private final int score;

    protected RunMeld(int score) {
        this.score = score;
    }

    /**
     * Returns the score of the meld
     * @return The score of the meld
     */
    @Override
    public int getScore() {
        return score;
    }

    /**
     * Creates a List of cards in String format that represent a run in a given suit
     *
     * @param suit Suit of the run
     * @return List of cards in String format that represent a run in the given suit
     */
    protected List<String> createRunCards(String suit) {
        List<String> runCards = new ArrayList<>();
        runCards.add(Rank.ACE.getRankCardValue() + suit);
        runCards.add(Rank.TEN.getRankCardValue() + suit);
        runCards.add(Rank.KING.getRankCardValue() + suit);
        runCards.add(Rank.QUEEN.getRankCardValue() + suit);
        runCards.add(Rank.JACK.getRankCardValue() + suit);
        return runCards;
    }
}