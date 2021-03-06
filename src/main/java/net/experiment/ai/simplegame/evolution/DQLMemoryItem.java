package net.experiment.ai.simplegame.evolution;

import net.experiment.ai.simplegame.game.GameState;

public class DQLMemoryItem {
    public final GameState state;
    public final int direction;
    public final int reward;
    public final GameState stateNext;
    public final boolean win;

    public DQLMemoryItem(GameState state, int direction, int reward, GameState stateNext, boolean win) {
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
