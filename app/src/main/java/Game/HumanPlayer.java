package Game;

import card.Suit;
import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.CardAdapter;
import ch.aplu.jcardgame.CardListener;
import ch.aplu.jgamegrid.*;

import java.util.List;

import static utils.Constants.BID_SELECTION_VALUE;
import static utils.Constants.MAX_SINGLE_BID;

/** The human player receives input from the player from buttons on the GUI and advances the game
 * using the selected inputs */
public class HumanPlayer extends Player {

    private final GUI guiRef;

    // Bidding
    private boolean activatedBidButtons = false;
    boolean hasHumanBid = false;
    private int humanBid = 0;
    private int currBid;

    // Trump
    private String trumpSuit = null;
    private Card selectedCard;

    // Cutthroat
    private Card selectedCutthroatCard = null;
    private boolean ctSelectPhase;
    private boolean hasInitCT = false;
    private Card discardCard;
    private Card clickedCard = null;
    private boolean isDiscardPhase = false;

    // Trick taking
    private boolean hasInitCards = false;

    HumanPlayer(GUI guiRef, int playerIndex) {
        this.guiRef = guiRef;
        this.playerIndex = playerIndex;
    }

    /** Enable listening for input for the bidding buttons */
    private void initializeBidButtons() {
        guiRef.getBidSelectionButton().addButtonListener(new GGButtonListener() {
            @Override
            public void buttonPressed(GGButton ggButton) {
                hasHumanBid = false;

                if (humanBid >= MAX_SINGLE_BID) {
                    guiRef.getBidSelectionButton().setActEnabled(false);
                    Pinochle.getGameRef().setStatus("Maximum amount of a single bid reached");
                } else {
                    humanBid += BID_SELECTION_VALUE;
                }
                guiRef.updateBidText(playerIndex, humanBid + currBid, currBid);
            }

            @Override
            public void buttonReleased(GGButton ggButton) {
            }

            @Override
            public void buttonClicked(GGButton ggButton) {
            }
        });

        guiRef.getBidConfirmButton().addButtonListener(new GGButtonListener() {
            @Override
            public void buttonPressed(GGButton ggButton) {
                hasHumanBid = true;
                guiRef.updateBidText(playerIndex, humanBid + currBid, humanBid + currBid);
                Pinochle.getGameRef().setStatus("");
            }

            @Override
            public void buttonReleased(GGButton ggButton) {
            }

            @Override
            public void buttonClicked(GGButton ggButton) {
            }
        });

        guiRef.getBidPassButton().addButtonListener(new GGButtonListener() {
            @Override
            public void buttonPressed(GGButton ggButton) {
                guiRef.updateBidText(playerIndex, 0, currBid);
                humanBid = 0;
                hasHumanBid = true;
                Pinochle.getGameRef().setStatus("");
            }

            @Override
            public void buttonReleased(GGButton ggButton) {
            }

            @Override
            public void buttonClicked(GGButton ggButton) {
            }
        });
    }


    /** Wait for the user to enter a bid */
    @Override
    public int getBiddingChoice(int currBid) {
        this.currBid = currBid;
        if (!activatedBidButtons) {
            initializeBidButtons();
            activatedBidButtons = true;
        }

        humanBid = 0;
        hasHumanBid = false;
        guiRef.displayBidButtons(true);
        guiRef.updateBidText(playerIndex, 0, currBid);
        while (!hasHumanBid) GameGrid.delay(Pinochle.getGameRef().getDelayTime());
        return humanBid;
    }


    /** Wait for the user to click on a suit */
    @Override
    public String getTrumpCardChoice() {
        trumpSuit = null;
        guiRef.initTrumpPhase();

        GGButtonListener buttonListener = new GGButtonListener() {
            @Override
            public void buttonPressed(GGButton ggButton) {
                if (ggButton.equals(guiRef.getClubTrumpButton())) {
                    trumpSuit = Suit.CLUBS.getSuitShortHand();
                } else if (ggButton.equals(guiRef.getSpadeTrumpButton())) {
                    trumpSuit = Suit.SPADES.getSuitShortHand();
                } else if (ggButton.equals(guiRef.getHeartTrumpButton())) {
                    trumpSuit = Suit.HEARTS.getSuitShortHand();
                } else if (ggButton.equals(guiRef.getDiamondTrumpButton())) {
                    trumpSuit = Suit.DIAMONDS.getSuitShortHand();
                }
            }

            @Override public void buttonReleased(GGButton ggButton) {}
            @Override public void buttonClicked(GGButton ggButton) {}
        };
        guiRef.getClubTrumpButton().addButtonListener(buttonListener);
        guiRef.getSpadeTrumpButton().addButtonListener(buttonListener);
        guiRef.getHeartTrumpButton().addButtonListener(buttonListener);
        guiRef.getDiamondTrumpButton().addButtonListener(buttonListener);

        while (trumpSuit == null) GameGrid.delay(Pinochle.getGameRef().getDelayTime());

        guiRef.terminateTrumpPhase();
        return trumpSuit;
    }


    /** Wait for the user to select one of the cutthroat cards */
    @Override
    public Card getCutThroatChoice(List<Card> revealedCards) {
        selectedCutthroatCard = null;
        ctSelectPhase = true;
        Pinochle.getGameRef().getTable().getPlayingArea().setTouchEnabled(true);

        // Interaction logic for choosing card
        Pinochle.getGameRef().getTable().getPlayingArea().addCardListener(new CardAdapter() {
            public void leftDoubleClicked(Card card) {
                if (ctSelectPhase && selectedCutthroatCard == null) {
                    selectedCutthroatCard = card;
                }
            }
        });

        while (selectedCutthroatCard == null) GameGrid.delay(Pinochle.getGameRef().getDelayTime());

        ctSelectPhase = false;
        Pinochle.getGameRef().getTable().getPlayingArea().setTouchEnabled(false);
        return selectedCutthroatCard;
    }

    /** Initializes the cards for discarding during the cutthroat phase */
    private void initCutThroatDiscard() {
        hand.setTouchEnabled(true);
        isDiscardPhase = true;

        hand.addCardListener(new CardAdapter() {
            int yPos = 0;
            final int yOffset = 50;
            /**
             * Left-click handler for cut-throat card discarding. Moves the selected card along the Y-axis
             * to make it easier to select the card for discarding and prevent multiple discards in one double-click
             *
             * @param card The selected card
             */
            /*
            public void leftClicked(Card card) {
                // If the previously selected card wasn't discarded, move it back to its original position
                if (clickedCard != null) {
                    Actor prevCard = clickedCard.getCardActor();
                    if (prevCard != null && prevCard.isInGrid()) {
                        prevCard.setLocation(new Location(prevCard.getLocation().x, yPos));
                    }
                }

                // The selected card is stored for later
                clickedCard = card;
                // Get the Actor for the selected card
                Actor cardActor = card.getCardActor();
                // If the card wasn't discarded previously, store its original Y-position for later BEFORE applying
                // the Y offset
                if (cardActor != null && cardActor.isInGrid()) {
                    yPos = cardActor.getLocation().getY();
                    cardActor.setLocation(new Location(cardActor.getLocation().getX(), yPos + yOffset));
                }
            }*/

            public void leftDoubleClicked(Card card) {
                if (isDiscardPhase) {
                    discardCard = card;
                }
            }
        });
    }

    /** Wait for the user to select a card to discard */
    @Override
    public Card getCutThroatDiscardChoice(int remainingCards) {
        if (!hasInitCT) {
            initCutThroatDiscard();
            hasInitCT = true;
        }

        discardCard = null;
        clickedCard = null;
        while (discardCard == null) GameGrid.delay(Pinochle.getGameRef().getDelayTime());

        if (remainingCards == 1) {
            hand.setTouchEnabled(false);
            isDiscardPhase = false;
        }

        return discardCard;
    }


    /** Initializes the player's card for trick taking */
    private void initCards() {
        // Set up human player for interaction
        CardListener cardListener = new CardAdapter()  // Human Player plays card
        {
            public void leftDoubleClicked(Card card) {
                if (!TrickTakingManager.checkValidTrick(card, hand.getCardList(),
                        Pinochle.getGameRef().getTable().getPlayingArea().getCardList())) {
                    Pinochle.getGameRef().setStatus(
                            "Card is not valid. Player needs to choose higher card of the same suit or trump suit");
                    return;
                }
                selectedCard = card;
                hand.setTouchEnabled(false);
            }
        };
        hand.addCardListener(cardListener);
    }

    /** Wait for the user to select a card during trick taking phase */
    @Override
    public Card getTrickTakingChoice(List<Card> placedCards) {
        if (!hasInitCards) {
            initCards();
            hasInitCards = true;
        }

        Pinochle.getGameRef().setStatus(
                "Player " + playerIndex + " is playing. Please double click on a card to discard");

        selectedCard = null;
        hand.setTouchEnabled(true);
        while (selectedCard == null) GameGrid.delay(Pinochle.getGameRef().getDelayTime());

        return selectedCard;
    }
}
