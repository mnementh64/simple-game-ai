package net.experiment.ai.simplegame.player;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.experiment.ai.simplegame.brain.Brain;
import net.experiment.ai.simplegame.brain.PerceptronBrain;
import net.experiment.ai.simplegame.game.GameWorld;
import net.experiment.ai.simplegame.geometry.GameBoardPosition;

public class PerceptronBrainPlayer extends Player implements AutomatedPlayer {

    @JsonProperty
    private Brain brain;
    private final String name;
    private final GameWorld.Direction[] directions;
    private int moveIndex = 0;
    private boolean replay;

    public PerceptronBrainPlayer(GameWorld gameWorld, int maxMoves) throws Exception {
        super(gameWorld, maxMoves);

        brain = new PerceptronBrain(gameWorld);
        this.name = "Player " + id;
        this.directions = new GameWorld.Direction[maxMoves];
        this.replay = false;
    }

    public PerceptronBrainPlayer(GameWorld gameWorld, int maxMoves, PerceptronBrain brain) {
        super(gameWorld, maxMoves);

        this.brain = brain;
        this.name = "Player " + id;
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

    public Brain getBrain() {
        return brain;
    }

    public void setBrain(Brain brain) {
        this.brain = brain;
    }
}
