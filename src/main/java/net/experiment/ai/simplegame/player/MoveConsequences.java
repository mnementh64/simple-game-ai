package net.experiment.ai.simplegame.player;

public class MoveConsequences {
    public boolean win;
    public int reward;

    public MoveConsequences(boolean win, int reward) {
        this.win = win;
        this.reward = reward;
    }
}
