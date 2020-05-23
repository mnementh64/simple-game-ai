package net.experiment.ai.simplegame.player;

import net.experiment.ai.simplegame.game.GameWorld;

public class DQLearningSolver extends AutomatedPlayer {
    public DQLearningSolver(GameWorld gameWorld, int maxMoves) {
        super(gameWorld, maxMoves);
    }
}
