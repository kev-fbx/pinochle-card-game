package AI;

/**
 * The cutthroat component is used to generate moves for the cutthroat phase of the game
 * */
public abstract class AICutThroatComponent extends AIComponent {
    protected boolean discardPhase = false;

    public void setDiscardPhase(boolean discardPhase) {
        this.discardPhase = discardPhase;
    }
}
