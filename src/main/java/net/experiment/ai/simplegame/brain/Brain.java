package net.experiment.ai.simplegame.brain;

import net.experiment.ai.simplegame.game.GameWorld;
import net.experiment.ai.simplegame.geometry.GameBoardPosition;

public abstract class Brain {

    public abstract GameWorld.Direction computeNextMove(double[] visualInformations, GameBoardPosition position) throws Exception;

    public abstract Brain crossover(Brain brain2) throws Exception;

    public abstract void mutate(double mutationRate, boolean applyExtremValue, double minValue, double maxValue);
}
