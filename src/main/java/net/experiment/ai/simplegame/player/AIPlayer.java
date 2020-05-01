package net.experiment.ai.simplegame.player;

import net.experiment.ai.simplegame.brain.Brain;
import net.experiment.ai.simplegame.brain.NNBrain;
import net.experiment.ai.simplegame.game.GameWorld;

public class AIPlayer extends Player implements AutomatedPlayer {

    private Brain brain;
    private final String name;

    public AIPlayer(GameWorld gameWorld, String name) throws Exception {
        super(gameWorld);

        brain = new NNBrain(gameWorld);
        this.name = name;
    }

    public GameWorld.Direction computeNextMove() throws Exception {
        // gather inputs to feed the brain
        // look into all 8 directions and get walls / diamonds / output
        double[] visualInformations = gameWorld.getGameLevel().lookInAllDirections(position);

        return brain.computeNextMove(visualInformations, position);
    }

    @Override
    public String toString() {
        return "AIPlayer{" +
                "name='" + name + "' with " +
                super.performanceToString() +
                '}';
    }
}
