package net.experiment.ai.simplegame.player;

import net.experiment.ai.simplegame.game.GameWorld;

public interface AutomatedPlayer {

    GameWorld.Direction computeNextMove() throws Exception;

}
