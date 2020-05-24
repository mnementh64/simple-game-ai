package net.experiment.ai.simplegame.player;

public class ActionAndReward {
    public int reward;
    public final int direction;

    public ActionAndReward(int reward, int direction) {
        this.reward = reward;
        this.direction = direction;
    }
}
