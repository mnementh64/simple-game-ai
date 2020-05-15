package net.experiment.ai.simplegame.brain;

import net.experiment.ai.simplegame.game.GameWorld;
import net.experiment.ai.simplegame.geometry.GameBoardPosition;

public abstract class Brain {

    protected GameWorld gameWorld;

    public Brain(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
    }

    public abstract GameWorld.Direction computeNextMove(double[] visualInformations, GameBoardPosition position) throws Exception;

    public abstract Brain crossover(Brain brain2);

    public abstract void mutate(double mutationRate, boolean applyExtremValue, double minValue, double maxValue);
}
