package net.experiment.ai.simplegame.brain;

import net.experiment.ai.simplegame.game.GameBoardPosition;
import net.experiment.ai.simplegame.game.GameWorld;

public class RandomBrain extends Brain {

    public RandomBrain(GameWorld gameWorld) {
        super(gameWorld);
    }

    public GameWorld.Direction computeNextMove(boolean[] visualInformations, GameBoardPosition position) {
        int randomValue = (int) (Math.random() * 4 + 1);
        return randomValue == 1 ? GameWorld.Direction.UP :
                randomValue == 2 ? GameWorld.Direction.DOWN :
                        randomValue == 3 ? GameWorld.Direction.LEFT :
                                GameWorld.Direction.RIGHT;
    }
}
