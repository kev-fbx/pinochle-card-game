package AI;

/**
 * The different types of components that can be equipped by the AI
 * */
public enum AIType {
    SMART(0),
    RANDOM(1);

    private final int AIType;

    AIType(int type) {
        this.AIType = type;
    }

    public int getAIType() {
        return AIType;
    }
}
