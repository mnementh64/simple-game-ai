package net.experiment.ai.simplegame.player;

import net.experiment.ai.simplegame.brain.Brain;
import net.experiment.ai.simplegame.brain.NNBrain;
import net.experiment.ai.simplegame.game.GameWorld;
import net.experiment.ai.simplegame.geometry.GameBoardPosition;

public class AIPlayer extends Player implements AutomatedPlayer {

    private Brain brain;
    private final String name;
    private final int maxMoves;
    private final GameWorld.Direction[] directions;
    private int moveIndex = 0;
    private boolean replay;

    public AIPlayer(GameWorld gameWorld, String name, int maxMoves) throws Exception {
        super(gameWorld);

        brain = new NNBrain(gameWorld);
        this.name = name;
        this.maxMoves = maxMoves;
        this.directions = new GameWorld.Direction[maxMoves];
        this.replay = false;
    }

    @Override
    public void askedToMove(GameWorld.Direction direction) {
        if (!replay) {
            // register this direction for replay purpose
            directions[moveIndex++] = direction;
        }
        super.askedToMove(direction);
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

    public GameWorld.Direction nextReplayDirection() {
        return directions[moveIndex++];
    }

    public void startReplay(GameBoardPosition startPosition) {
        this.position = startPosition;
        this.replay = true;
        this.moveIndex = 0;
        reinit();
        this.cumulativeNbMoves = 0;
    }

    public void stopReplay() {
        this.replay = false;
        this.moveIndex = 0;
        reinit();
    }
}
