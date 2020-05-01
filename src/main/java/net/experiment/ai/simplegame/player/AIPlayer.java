package net.experiment.ai.simplegame.player;

import net.experiment.ai.simplegame.brain.Brain;
import net.experiment.ai.simplegame.brain.RandomBrain;
import net.experiment.ai.simplegame.game.GameBoardPosition;
import net.experiment.ai.simplegame.game.GameWorld;

public class AIPlayer extends Player implements AutomatedPlayer {

    private Brain brain;

    public AIPlayer(GameWorld gameWorld, GameBoardPosition startPosition) {
        super(gameWorld, startPosition);

        brain = new RandomBrain(gameWorld);
    }

    public GameWorld.Direction computeNextMove() {
        // gather inputs to feed the brain
        // look into all 8 directions and get walls / diamonds / output
        boolean[] visualInformations = gameWorld.getGameLevel().lookInAllDirections(position);

        return brain.computeNextMove(visualInformations, position);
    }
}
