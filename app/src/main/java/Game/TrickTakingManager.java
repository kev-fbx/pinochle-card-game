package Game;

import card.Rank;
import card.Suit;
import card.Table;
import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;
import utils.CardUtils;
import utils.Constants;
import utils.Logger;

import java.util.*;

public class TrickTakingManager {

    private static TrickTakingManager instance = null;
    private Table table;

    private final int trickWidth = 40;
    private final int handWidth = 400;
    private final Location playingLocation = new Location(350, 350);

    private Card selected;

    private final Location[] trickHandLocations = {
            new Location(75, 350),
            new Location(625, 350)
    };

    /** Singleton Functions */
    public static TrickTakingManager getTrickTakingManager() {
        if (instance == null) {
            instance = new TrickTakingManager();
        }
        return instance;
    }

    public static void resetInstance() {
        instance = new TrickTakingManager();
    }

    /** Runs the main trick taking logic */
    public void RunTrickTakingPhase(ArrayList<Player> players) {
        table = Pinochle.getGameRef().getTable();

        int nextPlayer = BiddingManager.getBiddingManager().getBidWinPlayerIndex();
        int numberOfCards = players.get(Constants.COMPUTER_PLAYER_INDEX).getHand().getNumberOfCards();

        // Log player hands
        Hand[] hands = new Hand[Constants.NB_PLAYERS];
        for (int i=0; i <Constants.NB_PLAYERS; i++) {
            hands[i] = players.get(i).getHand();
        }
        Logger.getInstance().addPlayerCardsToLog(Constants.NB_PLAYERS, hands);

        // Play until the players run out of cards
        for (int i = 0; i < numberOfCards; i++) {
            Logger.getInstance().addRoundInfoToLog(i);
            for (int j = 0; j < Constants.NB_PLAYERS; j++) {
                selected = players.get(nextPlayer).getTrickTakingChoice(table.getPlayingArea().getCardList());
                selected.removeFromHand(true);
                // Adds a delay in auto mode
                if (Pinochle.getGameRef().isAuto()) {
                    GameGrid.delay(Pinochle.getGameRef().getDelayTime());
                }

                // Add cards to the winner
                Logger.getInstance().addCardPlayedToLog(nextPlayer, selected);
                table.getPlayingArea().insert(selected, true);

                table.getPlayingArea().setView(Pinochle.getGameRef(), new RowLayout(playingLocation,
                        (table.getPlayingArea().getNumberOfCards() + 2) * trickWidth));
                table.getPlayingArea().draw();

                if (table.getPlayingArea().getCardList().size() == 2) {
                    GameGrid.delay(Pinochle.getGameRef().getDelayTime());
                    int trickWinPlayerIndex = checkWinner(nextPlayer);
                    transferCardsToWinner(trickWinPlayerIndex);
                    nextPlayer = trickWinPlayerIndex;
                } else {
                    nextPlayer = (nextPlayer + 1) % Constants.NB_PLAYERS;
                }
            }

        }

        updateTrickScore(players);
    }

    /**
     * Check Trick Taking logic
     */
    public static boolean checkValidTrick(Card playingCard, List<Card> playerCards, List<Card> existingCards) {
        if (existingCards.isEmpty()) {
            return true;
        }

        Suit playingSuit = (Suit) playingCard.getSuit();
        Rank playingRank = (Rank) playingCard.getRank();
        Card existingCard = existingCards.get(0);
        Suit existingSuit = (Suit) existingCard.getSuit();
        Rank existingRank = (Rank) existingCard.getRank();

        // Same Suit, Higher Rank, then valid
        if (playingSuit.getSuitShortHand().equals(existingSuit.getSuitShortHand()) &&
                playingRank.getRankCardValue() > existingRank.getRankCardValue()) {
            return true;
        }

        // If the chosen is not the same suit, higher rank and there is one, then not valid
        Card higherCard = CardUtils.getHigherCardFromList(existingCard, playerCards);
        if (higherCard != null) {
            return false;
        }

        boolean isExistingTrump = existingSuit.getSuitShortHand().equals(TrumpManager.getInstance().getTrumpSuit());
        boolean isPlayingTrump = playingSuit.getSuitShortHand().equals(TrumpManager.getInstance().getTrumpSuit());
        // If the current is trump, then there is already no trump card with higher rank.
        // Otherwise, the above if should return false.
        if (isExistingTrump) {
            return true;
        }

        // If the current is not trump card, then playing trump card is valid
        if (isPlayingTrump) {
            return true;
        }

        // If the current is not trump card, and we have a trump card,
        // but not having a same suit, higher rank card, then we have to play trump card
        Card trumpCard = getTrumpCard(playerCards);
        if (trumpCard != null) {
            return false;
        }

        // If we dont have a trump card, any card is valid
        return true;
    }

    private static Card getTrumpCard(List<Card> cards) {
        return cards.stream().filter(playerCard -> {
            Suit playerCardSuit = (Suit) playerCard.getSuit();
            return playerCardSuit.getSuitShortHand().equals(TrumpManager.getInstance().getTrumpSuit());
        }).findAny().orElse(null);
    }

    /** Checks for the winner of the trick taking turn */
    private int checkWinner(int playerIndex) {
        assert (table.getPlayingArea().getCardList().size() == 2);
        int previousPlayerIndex = Math.abs(playerIndex - 1) % 2;
        Card card1 = table.getPlayingArea().getCardList().get(0);
        Card card2 = table.getPlayingArea().getCardList().get(1);
        System.out.println();
        System.out.print("Card1:");
        System.out.print(((Rank) card1.getRank()).getShortHandValue() + ((Suit) card1.getSuit()).getSuitShortHand() + " ");
        System.out.print("playBy:");
        System.out.print(previousPlayerIndex);
        System.out.print("  Card2:");
        System.out.print(((Rank) card2.getRank()).getShortHandValue() + ((Suit) card2.getSuit()).getSuitShortHand() + " ");
        System.out.print("playBy:");
        System.out.print(playerIndex);
        System.out.print("     ");

        boolean isHigherRankSameSuit = CardUtils.isSameSuit(card1, card2) && CardUtils.isHigherRank(card2, card1);
        System.out.println();
        if(CardUtils.isSameSuit(card1, card2)) {
            System.out.println("Is Same Suit");
        }if(CardUtils.isSameSuit(card1, card2) && !CardUtils.isHigherRank(card2, card1)) {
            System.out.println("But Not Higher Rank");
        }
        if (isHigherRankSameSuit) {

            System.out.println("winnerPlayer same suit by rank " + playerIndex);
            return playerIndex;
        }

        Suit card1Suit = (Suit) card1.getSuit();
        if (card1Suit.getSuitShortHand().equals(TrumpManager.getInstance().getTrumpSuit())) {
            System.out.println("winnerPlayer different suit by trump " + previousPlayerIndex);
            return previousPlayerIndex;
        }

        Suit card2Suit = (Suit) card2.getSuit();
        if (card2Suit.getSuitShortHand().equals(TrumpManager.getInstance().getTrumpSuit())) {
            System.out.println("winnerPlayer different suit by trump " + playerIndex);
            return playerIndex;
        }

        System.out.println("winnerPlayer by starting " + previousPlayerIndex);
        return previousPlayerIndex;
    }

    /** Gives the cards to the player that won the trick taking turn */
    private void transferCardsToWinner(int trickWinPlayerIndex) {
        for (Card card : table.getPlayingArea().getCardList()) {
            table.getTrickWinningHands()[trickWinPlayerIndex].insert(card, true);
        }
        table.getPlayingArea().removeAll(true);
        RowLayout[] trickHandLayouts = new RowLayout[Constants.NB_PLAYERS];
        GameGrid.delay(Pinochle.getGameRef().getDelayTime());
        for (int i = 0; i < Constants.NB_PLAYERS; i++) {
            trickHandLayouts[i] = new RowLayout(trickHandLocations[i], handWidth);
            trickHandLayouts[i].setRotationAngle(90);
            table.getTrickWinningHands()[i].setView(Pinochle.getGameRef(), trickHandLayouts[i]);
            table.getTrickWinningHands()[i].draw();
        }

        GameGrid.delay(Pinochle.getGameRef().getDelayTime());
    }

    /** Updates the score of the players on the GUI */
    private void updateTrickScore(ArrayList<Player> players) {
        for (int i = 0; i < Constants.NB_PLAYERS; i++) {
            List<Card> cards = table.getTrickWinningHands()[i].getCardList();
            int score = 0;
            for (Card card : cards) {
                Rank rank = (Rank) card.getRank();
                Suit suit = (Suit) card.getSuit();
                boolean isNineCard = rank.getRankCardValue() == Rank.NINE.getRankCardValue();
                boolean isTrumpCard = suit.getSuitShortHand().equals(TrumpManager.getInstance().getTrumpSuit());
                if (isNineCard && isTrumpCard) {
                    score += Rank.NINE_TRUMP;
                } else {
                    score += rank.getScoreValue();
                }
            }

            players.get(i).setScore(players.get(i).getScore() + score);
            if (i == BiddingManager.getBiddingManager().getBidWinPlayerIndex()) {
                if (players.get(i).getScore() < BiddingManager.getBiddingManager().getCurrentBid()) {
                    players.get(i).setScore(0);
                }
            }
        }
    }
}
