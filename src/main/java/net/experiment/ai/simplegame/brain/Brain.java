package net.experiment.ai.simplegame.brain;

import net.experiment.ai.simplegame.game.GameBoardPosition;
import net.experiment.ai.simplegame.game.GameWorld;

public abstract class Brain {

    protected final GameWorld gameWorld;

    public Brain(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
    }

    public abstract GameWorld.Direction computeNextMove(boolean[] visualInformations, GameBoardPosition position);
}
