package net.experiment.ai.simplegame.player;

import net.experiment.ai.simplegame.game.GameState;

public class MoveConsequences {
    public final GameState state;
    public final ActionAndReward actionAndReward;
    public boolean win;


    public MoveConsequences(GameState state, ActionAndReward actionAndReward, boolean win) {
        this.state = state;
        this.actionAndReward = actionAndReward;
        this.win = win;
        if (win) {
            this.actionAndReward.reward *= -1;
        }
    }
}
