package net.experiment.ai.simplegame.evolution;

public class DQLMemoryItem {
    public final int[][] state;
    public final int direction;
    public final int reward;
    public final int[][] stateNext;
    public final boolean win;

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
