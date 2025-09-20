package Game;

import ch.aplu.jgamegrid.*;
import utils.Constants;
import utils.Logger;

import java.util.*;

/**
 * BiddingManager handles the bidding phase logic of the game.
 *
 * @author Kevin Tran
 */
public class BiddingManager {
    private static BiddingManager instance = null;

    private final GUI guiRef;

    private int currentBid = 0;
    private boolean hasHumanPassed = false;
    private boolean hasComputerPassed = false;
    private int bidWinPlayerIndex = 0;

    /** Singleton Functions */
    private BiddingManager() {
        guiRef = Pinochle.getGameRef().getGui();
    }

    public static BiddingManager getBiddingManager() {
        if (instance == null) {
            instance = new BiddingManager();
        }
        return instance;
    }

    public static void resetInstance() {
        instance = new BiddingManager();
    }

    /** Bidding Logic */
    public void runBiddingPhase(ArrayList<Player> players) {
        guiRef.initBiddingPhase();
        guiRef.displayBidButtons(false);

        String bidOrder = Pinochle.getGameRef().getProperties().getProperty("players.bid_first", "random");

        boolean isContinueBidding = true;
        guiRef.updateBidText(Constants.INITIAL_CALL, 0, currentBid);
        Random rand = new Random(1);
        int playerIndex = switch (bidOrder) {
            case Constants.RANDOM_BID -> rand.nextInt(Constants.NB_PLAYERS);
            case Constants.COMPUTER_BID -> Constants.COMPUTER_PLAYER_INDEX;
            case Constants.HUMAN_BID -> Constants.HUMAN_PLAYER_INDEX;
            default -> Constants.COMPUTER_PLAYER_INDEX;
        };

        while (isContinueBidding) {
            for (int i = 0; i < Constants.NB_PLAYERS; i++) {
                askForBidForPlayerIndex(playerIndex, players);
                playerIndex = (playerIndex + 1) % Constants.NB_PLAYERS;
                isContinueBidding = !hasHumanPassed && !hasComputerPassed;
                if (!isContinueBidding) {
                    bidWinPlayerIndex = playerIndex;
                    break;
                }
            }
        }

        guiRef.removeBids();
        guiRef.updateBidResult(currentBid, bidWinPlayerIndex);
        Logger.getInstance().addBidInfoToLog(bidWinPlayerIndex, currentBid);
    }

    /** Request the player for a bid */
    private void askForBidForPlayerIndex(int playerIndex, ArrayList<Player> players) {
        int bidValue;
        bidValue = players.get(playerIndex).getBiddingChoice(currentBid);

        if (playerIndex == Constants.COMPUTER_PLAYER_INDEX) {
            guiRef.updateBidText(playerIndex, currentBid + bidValue, currentBid);
            GameGrid.delay(Pinochle.getGameRef().getThinkingTime());
            if (bidValue == 0) {
                hasComputerPassed = true;
                return;
            }
        } else {
            if (bidValue == 0) {
                hasHumanPassed = true;
            }
        }

        currentBid += bidValue;
        guiRef.updateBidText(playerIndex, 0, currentBid);
    }

    public int getBidWinPlayerIndex() {
        return bidWinPlayerIndex;
    }

    public int getCurrentBid() {
        return currentBid;
    }
}