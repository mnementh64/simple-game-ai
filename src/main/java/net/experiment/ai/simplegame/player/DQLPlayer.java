package net.experiment.ai.simplegame.player;

import net.experiment.ai.simplegame.game.GameWorld;

public class DQLPlayer extends AutomatedPlayer {
    public DQLPlayer(GameWorld gameWorld, int maxMoves) {
        super(gameWorld, maxMoves);
    }

    @Override
    public GameWorld.Direction computeNextMove() throws Exception {
        return super.computeNextMove();
    }

    public void remember(int[][] state, int direction, int reward, int[][] stateNext, boolean win) {

    }

    public void experienceReplay() {


    }
}
