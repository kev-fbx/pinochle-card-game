package AI;

/**
 * The AIBuilder is used to create an AI player using different components
 * */
public class AIBuilder {

    private AIBiddingComponent biddingComponent;
    private AITrickTakingComponent trickTakingComponent;
    private  AICutThroatComponent cutThroatComponent;

    public AIBuilder() {
        // Initialize default AI components
        biddingComponent = new SmartAIBidding();
        trickTakingComponent = new SmartAITrickTaking();
        cutThroatComponent = new RandomAICutThroat();
    }

    /** Select which bidding component to use for the AI */
    public void selectBiddingComponent(AIType type) {
        switch (type) {
            case SMART:
                biddingComponent = new SmartAIBidding();
                break;
            case RANDOM:
                biddingComponent = new RandomAIBidding();
                break;
        }
    }

    /** Select which trick taking component to use for the AI */
    public void selectTrickTakingComponent(AIType type) {
        switch (type) {
            case SMART:
                trickTakingComponent = new SmartAITrickTaking();
                break;
            case RANDOM:
                trickTakingComponent = new RandomAITrickTaking();
                break;
        }
    }

    /** Select which cutthroat component to use for the AI */
    public void selectCutThroatComponent(AIType type) {
        // Currently there is only 1 type of cutthroat component
        switch (type) {
            case SMART:
                cutThroatComponent = new RandomAICutThroat();
                break;
            default:
                cutThroatComponent = new RandomAICutThroat();
                break;
        }
    }

    /** Builds the AI with the selected components */
    public AIPlayer BuildAI() {
        return new AIPlayer(biddingComponent, trickTakingComponent, cutThroatComponent);
    }
}
