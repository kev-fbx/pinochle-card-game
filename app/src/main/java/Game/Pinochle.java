package Game;
import AI.AIBuilder;
import AI.AIPlayer;
import AI.AIType;

import card.*;
import ch.aplu.jcardgame.*;

import java.awt.*;
import java.util.*;
import java.util.List;

import meld.MeldCalculator;
import utils.*;

@SuppressWarnings("serial")
public class Pinochle extends CardGame {
    static public final int seed = 30008;
    static final Random random = new Random(seed);

    private final Properties properties;

    // Game reference
    private static Pinochle gameRef;

    // Game state
    private GameState gameState;

    //GUI
    private final GUI gui = new GUI();

    private final String version = "1.0";

    private final DeckFacade deck = new DeckFacade(Suit.values(), Rank.values(), "cover");

    Font smallFont = new Font("Arial", Font.BOLD, 18);


    private final int thinkingTime;
    private final int delayTime;

    private Table table;
    private final boolean ifCutThroatMode;

    private final boolean isAuto;

    public void setStatus(String string) {
        setStatusText(string);
    }

    /**
     * Game
     */
    private void initGame() {
        // Initializes AI computer player
        AIBuilder aiBuilder = new AIBuilder();
        String useSmartBid = properties.getProperty("players.0.smartbids", "false");
        if (useSmartBid.equalsIgnoreCase("true")) {
            aiBuilder.selectBiddingComponent(AIType.SMART);
        } else {
            aiBuilder.selectBiddingComponent(AIType.RANDOM);
        }
        String useSmartTrick = properties.getProperty("mode.smarttrick", "false");
        if (useSmartTrick.equalsIgnoreCase("true")) {
            aiBuilder.selectTrickTakingComponent(AIType.SMART);
        } else {
            aiBuilder.selectTrickTakingComponent(AIType.RANDOM);
        }
        aiBuilder.selectCutThroatComponent(AIType.RANDOM);
        AIPlayer aiPlayer = aiBuilder.BuildAI();
        aiPlayer.setPlayerIndex(Constants.COMPUTER_PLAYER_INDEX);


        // Setup fixed player
        ArrayList<Player> players = new ArrayList<>();
        for (int i = 0; i < Constants.NB_PLAYERS; i++) {
            players.add(null);
        }
        if (isAuto) {
            List<List<String>> playerTrickMovements = PropertyParser.setupPlayerAutoMovements();
            List<List<String>> playerCutthroatMovements = PropertyParser.parseCutthroatMoves();
            players.set(Constants.COMPUTER_PLAYER_INDEX,
                    new FixedPlayer(PropertyParser.parseBids(
                            properties.getProperty("players.0.bids", "")),
                            playerTrickMovements.get(Constants.COMPUTER_PLAYER_INDEX),
                            playerCutthroatMovements.get(Constants.COMPUTER_PLAYER_INDEX),
                            aiPlayer));
            players.set(Constants.HUMAN_PLAYER_INDEX,
                    new FixedPlayer(PropertyParser.parseBids(
                            properties.getProperty("players.1.bids", "")),
                            playerTrickMovements.get(Constants.HUMAN_PLAYER_INDEX),
                            playerCutthroatMovements.get(Constants.HUMAN_PLAYER_INDEX),
                            aiBuilder.BuildAI()));
        }

        // Setup regular players
        else {
            players.set(Constants.COMPUTER_PLAYER_INDEX, aiPlayer);
            players.set(Constants.HUMAN_PLAYER_INDEX, new HumanPlayer(gui, Constants.HUMAN_PLAYER_INDEX));
        }


        // Initializes game state
        gameState = new GameState(this, players, ifCutThroatMode);


        // Initialize hand for players
        for (int i = 0; i < Constants.NB_PLAYERS; i++) {
            players.get(i).setHand(new Hand(deck.getDeck()));
        }

        table = new Table(Constants.NB_PLAYERS, deck.getDeck());
        ScoreManager.getScoreManager().initScores(players);
    }

    private void playGame() {
        gameState.PlayGame();
    }

    public String runApp() {
        setTitle("Pinochle  (V" + version + ") Constructed for UofM SWEN30006 with JGameGrid (www.aplu.ch)");
        setStatusText("Initializing...");
        initGame();
        playGame();

        return Logger.getInstance().getLogResult().toString();
    }

    public Pinochle(Properties properties) {
        super(700, 700, 30);
        this.properties = properties;
        isAuto = Boolean.parseBoolean(properties.getProperty("isAuto", "false"));
        thinkingTime = Integer.parseInt(properties.getProperty("thinkingTime", "200"));
        delayTime = Integer.parseInt(properties.getProperty("delayTime", "50"));
        ifCutThroatMode = Boolean.parseBoolean(properties.getProperty("mode.cutthroat", "false"));

        gameRef = this;
        DealingManager.resetInstance();
        BiddingManager.resetInstance();
        TrumpManager.resetInstance();
        CutThroatManager.resetInstance();
        MeldCalculator.resetInstance();
        TrickTakingManager.resetInstance();
        ScoreManager.resetInstance();
        Logger.resetInstance();
    }

    public int getDelayTime() { return delayTime; }
    public int getThinkingTime() { return thinkingTime; }
    public Properties getProperties() {
        return properties;
    }

    public GUI getGui() {
        return gui;
    }

    public static Pinochle getGameRef() {
        return gameRef;
    }

    public Table getTable() {
        return table;
    }

    public boolean isAuto() {
        return isAuto;
    }

    public Random getRandom() { return random; }
}
