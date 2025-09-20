package Game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import ch.aplu.jcardgame.RowLayout;
import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;
import utils.CardUtils;
import utils.Constants;
import utils.PropertyParser;

import java.util.ArrayList;
import java.util.List;

public class CutThroatManager {

    /**
     * General attributes used across manager
     */
    private static CutThroatManager instance = null;
    private final Pinochle game;
    /**
     * Attributes for getting the top two cards from the stockpile
     */
    private final Location revealedCardsLocation = new Location(350, 350);
    private ArrayList<Player> players = new ArrayList<>();
    private Hand stockpile;
    private final List<Card> revealedCards = new ArrayList<>();
    /**
     * Attributes for the card selection phase
     */
    private int playerToSelectCard = -1;
    private Card selectedCard = null;
    /**
     * Attributes for the discard stage of cut-throat mode
     */
    private boolean remainingCardsDistributed = false;

    private CutThroatManager() {
        game = Pinochle.getGameRef();
    }

    public static CutThroatManager getCutThroatManager() {
        if (instance == null) {
            instance = new CutThroatManager();
        }
        return instance;
    }

    public static void resetInstance() {
        instance = new CutThroatManager();
    }

    /**
     * Entry point for the cut-throat stage
     *
     * @param players ArrayList of the players of the game
     */
    public void RunCutThroatPhase(ArrayList<Player> players) {
        this.players = players;
        this.stockpile = DealingManager.getDealingManager().getStockpile();

        // 1: Dealer flips top two cards from stockpile
        revealTwoCards();

        // 2: Bid winner chooses one card, loser gets the other card
        selectCards();

        // 3: Distribute remaining cards from the stockpile
        distributeRemainingCards();

        // 4: Each player discards 12 cards
        handleCardDiscarding();
    }

    /**
     * Runs the first stage of cut-throat mode: gets the top 2 cards from the stockpile and displays them
     * on the table's playing area.
     */
    private void revealTwoCards() {
        // Skip card revealing if it is on auto mode
        if (Pinochle.getGameRef().isAuto()) {
            return;
        }
        // Get the top 2 cards from the stockpile
        for (int i = 0; i < 2; i++) {
            Card card = stockpile.getFirst();
            card.removeFromHand(false);
            revealedCards.add(card);
        }

        // GUI for the top 2 cards
        game.getTable().getPlayingArea().insert(revealedCards.get(0), false);
        game.getTable().getPlayingArea().insert(revealedCards.get(1), false);
        game.getTable().getPlayingArea().setView(game,
                new RowLayout(revealedCardsLocation, 600));
        game.getTable().getPlayingArea().draw();
    }

    /**
     * Runs the logic for card selection in cut-throat mode. Checks which player won and allows them
     * to select from one of the 2 cards in the table playing area. The bid loser (dealer) receives the
     * remaining card.
     */
    private void selectCards() {
        // Skip card selection if it is on auto mode
        if (Pinochle.getGameRef().isAuto()) {
            return;
        }

        playerToSelectCard = BiddingManager.getBiddingManager().getBidWinPlayerIndex();

        // Request input from the player to select a card
        game.setStatus("Player" + playerToSelectCard + " must choose  a face-up card to have in their hand");
        selectedCard = players.get(playerToSelectCard).getCutThroatChoice(revealedCards);
        GameGrid.delay(game.getDelayTime());

        // Give the card to the player
        selectedCard.removeFromHand(false);
        players.get(playerToSelectCard).getHand().insert(selectedCard, false);

        Card remainingCard = revealedCards.stream()
                .filter(c -> c != selectedCard)
                .findFirst()
                .orElse(null);

        // Other player gets the remaining card
        playerToSelectCard = (playerToSelectCard + 1) % Constants.NB_PLAYERS;
        if (remainingCard != null) {
            remainingCard.removeFromHand(false);
            players.get(playerToSelectCard).getHand().insert(remainingCard, false);
        }

        // Clears out the selection stage
        game.getTable().getPlayingArea().removeAll(true);
        revealedCards.clear();
    }

    /**
     * Gets the remaining cards from the stockpile and gives them to the players in an alternating pattern
     */
    private void distributeRemainingCards() {
        if (remainingCardsDistributed) {
            return;
        }

        // Auto mode support
        if (Pinochle.getGameRef().isAuto()) {
            List<List<String>> cutthroatDistribution = PropertyParser.parseCutthroatDistribution();
            // Distribution cards to each player
            for (int i = 0; i < Constants.NB_PLAYERS; i++) {
                List<String> playerDistribution = cutthroatDistribution.get(i);
                for (String nextCard : playerDistribution) {
                    String[] cardStrings = nextCard.split("-");
                    String cardDealtString = cardStrings[0];
                    if (nextCard.isEmpty()) {
                        break;
                    }
                    Card dealt = CardUtils.getCardFromList(stockpile.getCardList(), cardDealtString);
                    if (dealt == null) {
                        System.err.println(
                                "cannot draw card: " + cardDealtString + " - hand: " + stockpile.getCardList());
                    } else {
                        dealt.removeFromHand(false);
                        players.get(i).getHand().insert(dealt, false);
                    }
                }
            }
        }
        // Random card distribution
        else {
            int currentReceiver = BiddingManager.getBiddingManager().getBidWinPlayerIndex();

            // Each player has received 1 card from the first two draws, so (12-1)*2 = 22 more cards
            // need to be drawn; 11 more cards are given to each player.
            int totalCardsToDistribute = (Constants.NUM_DISCARD_CARDS - 1) * 2;

            // Alternating between each player, draw a card from the stockpile and give it to them
            // until each player has 24 total cards.
            for (int i = 0; i < totalCardsToDistribute; i++) {
                Card drawnCard = stockpile.getFirst();
                drawnCard.removeFromHand(false);
                players.get(currentReceiver).getHand().insert(drawnCard, false);
                currentReceiver = (currentReceiver + 1) % Constants.NB_PLAYERS;
            }
        }

        // GUI for new cards
        for (int i = 0; i < Constants.NB_PLAYERS; i++) {
            players.get(i).getHand().sort(ch.aplu.jcardgame.Hand.SortType.SUITPRIORITY, false);
            players.get(i).getHand().draw();
        }

        remainingCardsDistributed = true;
    }

    /**
     * Handles the card discarding stage of cut-throat
     */
    private void handleCardDiscarding() {

        for (int i = 0; i < Constants.NUM_DISCARD_CARDS; i++) {
            for (int j = 0; j < Constants.NB_PLAYERS; j++) {
                game.setStatus("Player" + j + " must discard " + (Constants.NUM_DISCARD_CARDS-i) + " cards");
                Card discardCard = players.get(j).getCutThroatDiscardChoice(Constants.NUM_DISCARD_CARDS-i);
                discardCard.removeFromHand(false);
                assert players.get(j).getHand() != null;
                players.get(j).getHand().sort(Hand.SortType.SUITPRIORITY, false);
                players.get(j).getHand().draw();
                GameGrid.delay(Pinochle.getGameRef().getDelayTime());
            }
        }
    }
}