package net.experiment.ai.simplegame.evolution;

public class DQLMemoryItem {
    private final int[][] state;
    private final int direction;
    private final int reward;
    private final int[][] stateNext;
    private final boolean win;

    public DQLMemoryItem(int[][] state, int direction, int reward, int[][] stateNext, boolean win) {
        this.state = state;
        this.direction = direction;
        this.reward = reward;
        this.stateNext = stateNext;
        this.win = win;
    }

    public DQLMemoryItem(DQLMemoryItem itemModel) {
        this.state = itemModel.state;
        this.direction = itemModel.direction;
        this.reward = itemModel.reward;
        this.stateNext = itemModel.stateNext;
        this.win = itemModel.win;
    }
}
