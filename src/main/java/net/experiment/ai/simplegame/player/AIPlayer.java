package net.experiment.ai.simplegame.player;

import net.experiment.ai.simplegame.brain.Brain;
import net.experiment.ai.simplegame.brain.NNBrain;
import net.experiment.ai.simplegame.game.GameWorld;
import net.experiment.ai.simplegame.geometry.GameBoardPosition;

public class AIPlayer extends Player implements AutomatedPlayer {

    private Brain brain;

    public AIPlayer(GameWorld gameWorld, GameBoardPosition startPosition) throws Exception {
        super(gameWorld, startPosition);

        brain = new NNBrain(gameWorld);
    }

    public GameWorld.Direction computeNextMove() throws Exception {
        // gather inputs to feed the brain
        // look into all 8 directions and get walls / diamonds / output
        double[] visualInformations = gameWorld.getGameLevel().lookInAllDirections(position);

        return brain.computeNextMove(visualInformations, position);
    }
}
