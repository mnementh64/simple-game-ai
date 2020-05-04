package net.experiment.ai.simplegame.brain;

import net.experiment.ai.simplegame.geometry.GameBoardPosition;
import net.experiment.ai.simplegame.game.GameWorld;

public abstract class Brain {

    protected final GameWorld gameWorld;

    public Brain(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
    }

    public abstract GameWorld.Direction computeNextMove(double[] visualInformations, GameBoardPosition position) throws Exception;

    public abstract Brain mutate(double mutationRate);

    public abstract Brain crossover(Brain brain2);
}
