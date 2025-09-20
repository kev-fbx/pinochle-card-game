package Game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import utils.CardUtils;

import java.util.List;

/** The fixed player uses the fixed input from the properties file to play the game in a set way
 * */
public class FixedPlayer extends Player {

    // Preloaded moves for the player to make
    private final List<Integer> autoBids;
    private final List<String> autoTricks;
    private int nextTrickIndex;
    private final List<String> autoCutThroat;

    // Backup player is used when the fixed player runs out of moves to make
    private final Player backupPlayer;


    FixedPlayer(List<Integer> autoBids, List<String> autoTricks, List<String> autoCutThroat, Player backupPlayer) {
        this.autoBids = autoBids;
        this.backupPlayer = backupPlayer;
        this.autoTricks = autoTricks;
        this.autoCutThroat = autoCutThroat;
        nextTrickIndex = 0;
    }

    @Override
    public void setHand(Hand hand) {
        this.hand = hand;
        backupPlayer.setHand(hand);
    }

    /** Returns the move from the property file, using the backup if it is out of moves */
    @Override
    public int getBiddingChoice(int currBid) {
        if (!autoBids.isEmpty()) {
            return autoBids.remove(0);
        }

        return backupPlayer.getBiddingChoice(currBid);
    }

    /** Returns the move from the property file, using the backup if it is out of moves */
    @Override
    public String getTrumpCardChoice() {
        String suit = Pinochle.getGameRef().getProperties().getProperty("players.trump");
        if (suit != null) {
            return suit;
        }
        return backupPlayer.getTrumpCardChoice();
    }

    /** Returns the move from the property file, using the backup if it is out of moves */
    @Override
    public Card getCutThroatChoice(List<Card> revealedCards) {
        return backupPlayer.getCutThroatChoice(revealedCards);
    }

    /** Returns the move from the property file, using the backup if it is out of moves */
    @Override
    public Card getCutThroatDiscardChoice(int remainingCards) {
        if (autoCutThroat.isEmpty()) return backupPlayer.getCutThroatDiscardChoice(remainingCards);

        // Find a card that is not in the final cards
        for (int i=0; i < hand.getCardList().size(); i++) {
            Card discardedCard = hand.getCardList().get(i);

            // Compare to the cards in the final hand
            boolean shouldDiscard = true;
            for (String cardString : autoCutThroat) {
                String[] cardStrings = cardString.split("-");
                String card = cardStrings[0];
                if (cardString.isEmpty()) {
                    return backupPlayer.getCutThroatDiscardChoice(remainingCards);
                }
                Card finalCard = CardUtils.getCardFromList(hand.getCardList(), card);
                if (finalCard != null) {
                    // The current card should be in the final hand
                    if (finalCard.equals(discardedCard)) {
                        shouldDiscard = false;
                        break;
                    }
                }
            }

            // return the card that should be discarded
            if (shouldDiscard) {
                return discardedCard;
            }
        }

        return backupPlayer.getCutThroatDiscardChoice(remainingCards);
    }

    /** Returns the move from the property file, using the backup if it is out of moves */
    @Override
    public Card getTrickTakingChoice(List<Card> placedCards) {
        if (hand.isEmpty()) return backupPlayer.getTrickTakingChoice(placedCards);
        if (autoTricks.isEmpty()) return backupPlayer.getTrickTakingChoice(placedCards);

        if (nextTrickIndex < autoTricks.size()) {
            String nextMovement = autoTricks.get(nextTrickIndex);
            nextTrickIndex++;

            String[] cardStrings = nextMovement.split("-");
            String cardDealtString = cardStrings[0];
            if (nextMovement.isEmpty()) {
                return backupPlayer.getTrickTakingChoice(placedCards);
            }
            Card dealt = CardUtils.getCardFromList(hand.getCardList(), cardDealtString);
            if (dealt == null) {
                System.err.println("cannot draw card: " + cardDealtString + " - hand: " + hand.getCardList());
            } else {
                return dealt;
            }
        }
        return backupPlayer.getTrickTakingChoice(placedCards);
    }
}
