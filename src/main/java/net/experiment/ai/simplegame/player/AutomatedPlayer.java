package net.experiment.ai.simplegame.player;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.experiment.ai.simplegame.brain.Brain;
import net.experiment.ai.simplegame.brain.PerceptronBrain;
import net.experiment.ai.simplegame.game.GameWorld;
import net.experiment.ai.simplegame.geometry.GameBoardPosition;

import java.util.Arrays;

public abstract class AutomatedPlayer extends Player {

    @JsonProperty
    private Brain brain;

    private final GameWorld.Direction[] directions;
    private int moveIndex = 0;
    private boolean replay;
    private final String name;

    public AutomatedPlayer(GameWorld gameWorld, int maxMoves) {
        this(gameWorld, maxMoves, null);
    }

    public AutomatedPlayer(GameWorld gameWorld, int maxMoves, PerceptronBrain brain) {
        super(gameWorld, maxMoves);
        this.brain = brain;
        this.replay = false;
        this.directions = new GameWorld.Direction[maxMoves];
        this.name = "Player " + id;
    }

    @Override
    public void reinit() {
        this.moveIndex = 0;
        this.setScore(0);
        Arrays.fill(directions, null);
        super.reinit();
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

    @Override
    public String toString() {
        return "AIPlayer{" +
                "name='" + name + "' with " +
                super.performanceToString() +
                '}';
    }
}
