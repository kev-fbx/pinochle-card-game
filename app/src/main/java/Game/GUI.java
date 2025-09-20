package Game;

import ch.aplu.jcardgame.RowLayout;
import ch.aplu.jcardgame.TargetArea;
import ch.aplu.jgamegrid.*;
import utils.Constants;

import java.awt.*;
import java.util.ArrayList;

/**
 * The GUi class allows the user to interact with the managers without needing to access them
 * */
public class GUI {

    private Pinochle game;

    // General GUI
    private final Location playingLocation = new Location(350, 350);
    private final int handWidth = 400;
    private final int trickWidth = 40;
    private final Location[] handLocations = {
            new Location(350, 625),
            new Location(350, 75),
    };
    private final Location[] trickHandLocations = {
            new Location(75, 350),
            new Location(625, 350)
    };

    // Bidding GUI
    private final GGButton bidSelectionButton = new GGButton("sprites/bid_10.gif", false);
    private final GGButton bidConfirmButton = new GGButton("sprites/done30.gif", false);
    private final GGButton bidPassButton = new GGButton("sprites/bid_pass.gif", false);

    private final Location bidSelectionLocation = new Location(600, 100);
    private final Location bidConfirmLocation = new Location(660, 100);
    private final Location bidPassLocation = new Location(630, 150);
    private final Location playerBidLocation = new Location(550, 30);
    private final Location currentBidLocation = new Location(550, 50);
    private final Location newBidLocation = new Location(550, 75);

    private TextActor playerBidText;
    private TextActor currentBidText;
    private TextActor newBidText;

    // Trump GUI
    private final GGButton spadeTrumpButton = new GGButton("sprites/spades_item.png", false);
    private final GGButton diamondTrumpButton = new GGButton("sprites/diamonds_item.png", false);
    private final GGButton heartTrumpButton = new GGButton("sprites/hearts_item.png", false);
    private final GGButton clubTrumpButton = new GGButton("sprites/clubs_item.png", false);
    private final Location clubTrumpLocation = new Location(580, 100);
    private final Location spadeTrumpLocation = new Location(610, 100);
    private final Location diamondTrumpLocation = new Location(640, 100);
    private final Location heartTrumpLocation = new Location(670, 100);


    /** General GUI */
    public void initGUI(ArrayList<Player> players) {
        game = Pinochle.getGameRef();
        game.getTable().getPlayingArea().setView(game, new RowLayout(playingLocation,
                (game.getTable().getPlayingArea().getNumberOfCards() + 3) * trickWidth));
        game.getTable().getPlayingArea().draw();

        // graphics
        RowLayout[] layouts = new RowLayout[Constants.NB_PLAYERS];
        for (int i = 0; i < Constants.NB_PLAYERS; i++) {
            layouts[i] = new RowLayout(handLocations[i], handWidth);
            layouts[i].setRotationAngle(180 * i);
            players.get(i).getHand().setView(Pinochle.getGameRef(), layouts[i]);
            players.get(i).getHand().setTargetArea(new TargetArea(playingLocation));
            players.get(i).getHand().draw();
        }

        RowLayout[] trickHandLayouts = new RowLayout[Constants.NB_PLAYERS];

        for (int i = 0; i < Constants.NB_PLAYERS; i++) {
            trickHandLayouts[i] = new RowLayout(trickHandLocations[i], handWidth);
            trickHandLayouts[i].setRotationAngle(90 + 180 * i);
            game.getTable().getTrickWinningHands()[i].setView(game, trickHandLayouts[i]);
            game.getTable().getTrickWinningHands()[i].draw();
        }

    }


    /** Bidding GUI */
    public GGButton getBidPassButton() {
        return bidPassButton;
    }

    public GGButton getBidConfirmButton() {
        return bidConfirmButton;
    }

    public GGButton getBidSelectionButton() {
        return bidSelectionButton;
    }

    public void initBiddingPhase() {
        game = Pinochle.getGameRef();
        playerBidText = new TextActor("Bidding", Color.white, game.bgColor, game.smallFont);
        currentBidText = new TextActor("Current Bid: ", Color.white, game.bgColor, game.smallFont);
        newBidText = new TextActor("New Bid: ", Color.white, game.bgColor, game.smallFont);

        game.addActor(bidSelectionButton, bidSelectionLocation);
        game.addActor(bidConfirmButton, bidConfirmLocation);
        game.addActor(bidPassButton, bidPassLocation);

        game.addActor(playerBidText, playerBidLocation);
        game.addActor(currentBidText, currentBidLocation);
        game.addActor(newBidText, newBidLocation);

        game.setActorOnTop(bidSelectionButton);
        game.setActorOnTop(bidConfirmButton);
        game.setActorOnTop(bidPassButton);

        bidSelectionButton.setActEnabled(false);
        bidConfirmButton.setActEnabled(false);
        bidPassButton.setActEnabled(false);

        this.playerBidText = new TextActor("Bidding", Color.white, game.bgColor, game.smallFont);
        this.currentBidText = new TextActor("Current Bid: ", Color.white, game.bgColor, game.smallFont);
        this.newBidText = new TextActor("New Bid: ", Color.white, game.bgColor, game.smallFont);
    }

    public void updateBidText(int playerIndex, int newBid, int currentBid) {
        game = Pinochle.getGameRef();
        String playerBidString = switch (playerIndex) {
            case Constants.INITIAL_CALL -> "Bid";
            case Constants.COMPUTER_PLAYER_INDEX -> "Computer Bid";
            case Constants.HUMAN_PLAYER_INDEX -> "Human Bid";
            default -> "";
        };

        removeBidText();
        currentBidText = new TextActor("Current Bid: " + currentBid, Color.WHITE, game.bgColor, game.smallFont);
        game.addActor(currentBidText, currentBidLocation);

        String newBidString = newBid == 0 ? "" : String.valueOf(newBid);
        newBidText = new TextActor("New Bid: " + newBidString, Color.WHITE, game.bgColor, game.smallFont);
        game.addActor(newBidText, newBidLocation);

        playerBidText = new TextActor(playerBidString, Color.WHITE, game.bgColor, game.smallFont);
        game.addActor(playerBidText, playerBidLocation);

        GameGrid.delay(game.getDelayTime());
    }

    public void displayBidButtons(boolean isShown) {
        bidSelectionButton.setActEnabled(isShown);
        bidConfirmButton.setActEnabled(isShown);
        bidPassButton.setActEnabled(isShown);
    }

    public void removeBids() {
        game = Pinochle.getGameRef();
        game.removeActor(bidSelectionButton);
        game.removeActor(bidConfirmButton);
        game.removeActor(bidPassButton);
        game.removeActor(newBidText);
    }

    public void removeBidText() {
        game = Pinochle.getGameRef();
        game.removeActor(currentBidText);
        game.removeActor(newBidText);
        game.removeActor(playerBidText);
    }

    public void updateBidResult(int currentBid, int bidWinPlayerIndex) {
        game = Pinochle.getGameRef();
        game.removeActor(playerBidText);
        game.removeActor(currentBidText);

        currentBidText = new TextActor("Current Bid: " + currentBid, Color.WHITE, game.bgColor, game.smallFont);
        game.addActor(currentBidText, currentBidLocation);

        String playerBidString = bidWinPlayerIndex == Constants.COMPUTER_PLAYER_INDEX ? "Computer Win" : "Human Win";
        playerBidText = new TextActor(playerBidString, Color.WHITE, game.bgColor, game.smallFont);
        game.addActor(playerBidText, playerBidLocation);
    }


    /** Trump GUI */
    public GGButton getSpadeTrumpButton() {
        return spadeTrumpButton;
    }

    public GGButton getDiamondTrumpButton() {
        return diamondTrumpButton;
    }

    public GGButton getHeartTrumpButton() {
        return heartTrumpButton;
    }

    public GGButton getClubTrumpButton() {
        return clubTrumpButton;
    }

    public void initTrumpPhase() {
        game = Pinochle.getGameRef();
        game.addActor(clubTrumpButton, clubTrumpLocation);
        game.addActor(spadeTrumpButton, spadeTrumpLocation);
        game.addActor(heartTrumpButton, heartTrumpLocation);
        game.addActor(diamondTrumpButton, diamondTrumpLocation);
    }
    public void terminateTrumpPhase() {
        game.removeActor(clubTrumpButton);
        game.removeActor(spadeTrumpButton);
        game.removeActor(heartTrumpButton);
        game.removeActor(diamondTrumpButton);
    }
}
