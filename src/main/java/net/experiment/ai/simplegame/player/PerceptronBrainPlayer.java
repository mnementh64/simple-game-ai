package net.experiment.ai.simplegame.player;

import net.experiment.ai.simplegame.brain.PerceptronBrain;
import net.experiment.ai.simplegame.game.GameWorld;

public class PerceptronBrainPlayer extends AutomatedPlayer {

    private final String name;

    public PerceptronBrainPlayer(GameWorld gameWorld, int maxMoves) throws Exception {
        super(gameWorld, maxMoves);

        setBrain(new PerceptronBrain(gameWorld));
        this.name = "Player " + id;
    }

    public PerceptronBrainPlayer(GameWorld gameWorld, int maxMoves, PerceptronBrain brain) {
        super(gameWorld, maxMoves);

        setBrain(brain);
        this.name = "Player " + id;
    }

    @Override
    public String toString() {
        return "AIPlayer{" +
                "name='" + name + "' with " +
                super.performanceToString() +
                '}';
    }
}
