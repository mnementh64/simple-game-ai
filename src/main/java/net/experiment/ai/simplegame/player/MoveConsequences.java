package net.experiment.ai.simplegame.player;

public class MoveConsequences {
    public final int[][] state;
    public final ActionAndReward actionAndReward;
    public boolean win;


    public MoveConsequences(int[][] state, ActionAndReward actionAndReward, boolean win) {
        this.state = state;
        this.actionAndReward = actionAndReward;
        this.win = win;
        if (win) {
            this.actionAndReward.reward *= -1;
        }
    }
}
