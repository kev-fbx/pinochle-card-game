package Game;

import ch.aplu.jcardgame.Hand;
import ch.aplu.jgamegrid.GameGrid;
import meld.MeldCalculator;
import utils.Constants;
import utils.Logger;

import java.util.ArrayList;

public class GameState {

    private GamePhase currPhase;

    private final boolean isCutThroatEnabled;

    private final Pinochle game;

    private final ArrayList<Player> players;


    GameState(Pinochle game, ArrayList<Player> players, boolean shouldEnableCutthroat) {
        isCutThroatEnabled = shouldEnableCutthroat;
        this.game = game;
        this.players = players;
        currPhase = GamePhase.DEALING;
    }

    /**  Updates the current phase of the game to the next phase */
    public void ChangePhase() {
        switch (currPhase) {
            case DEALING:
                currPhase = GamePhase.BIDDING;
                return;
            case BIDDING:
                if (isCutThroatEnabled) {
                    currPhase = GamePhase.CUT_THROAT;
                } else {
                    currPhase = GamePhase.MELDING;
                }
                return;
            case MELDING:
                currPhase = GamePhase.TRICK_TAKING;
                return;
            case TRICK_TAKING:
                currPhase = GamePhase.SCORING;
                return;
            case CUT_THROAT:
                currPhase = GamePhase.MELDING;
        }
    }


    /** Advances the game based on the current phase of the game */
    public void PlayGame() {
        switch (currPhase) {
            case DEALING:
                // Initialize player hands and deck
                DealingManager.getDealingManager().runDealingPhase(players);
                // Displays GUI
                game.getGui().initGUI(players);
                ChangePhase();
                PlayGame();
                return;

            case BIDDING:
                // Request input for bidding
                BiddingManager.getBiddingManager().runBiddingPhase(players);
                TrumpManager.getInstance().askForTrumpCard(players);
                ChangePhase();
                PlayGame();
                return;

            case CUT_THROAT:
                // Plays the cutthroat mode if it is enabled
                CutThroatManager.getCutThroatManager().RunCutThroatPhase(players);
                ChangePhase();
                PlayGame();
                return;

            case MELDING:
                // Calculate Melds for both players
                for (int i = 0; i < Constants.NB_PLAYERS; i++) {
                    Player player = players.get(i);
                    int score = MeldCalculator.getInstance().calculateMeldingScore(player.getHand().getCardList(),
                            TrumpManager.getInstance().getTrumpSuit());
                    player.setScore(score);
                    ScoreManager.getScoreManager().updateScore(players, i);
                    GameGrid.delay(game.getDelayTime());
                }
                int[] scores = new int[Constants.NB_PLAYERS];
                scores[0] = players.get(Constants.COMPUTER_PLAYER_INDEX).getScore();
                scores[1] = players.get(Constants.HUMAN_PLAYER_INDEX).getScore();
                Logger.getInstance().addTrumpInfoToLog(TrumpManager.getInstance().getTrumpSuit(), scores);
                ChangePhase();
                PlayGame();
                return;

            case TRICK_TAKING:
                // Log players' cards
                Hand[] hands = new Hand[Constants.NB_PLAYERS];
                hands[Constants.COMPUTER_PLAYER_INDEX] = players.get(Constants.COMPUTER_PLAYER_INDEX).getHand();
                hands[Constants.HUMAN_PLAYER_INDEX] = players.get(Constants.HUMAN_PLAYER_INDEX).getHand();
                Logger.getInstance().addPlayerCardsToLog(Constants.NB_PLAYERS, hands);

                // Request input from player
                for (int i = 0; i < Constants.NB_PLAYERS; i++) {
                    players.get(i).getHand().sort(Hand.SortType.SUITPRIORITY, false);
                }
                TrickTakingManager.getTrickTakingManager().RunTrickTakingPhase(players);
                ChangePhase();
                PlayGame();
                return;

            case SCORING:
                // Calculate score for players
                ScoreManager.getScoreManager().RunScoringPhase(players);
        }
    }
}
