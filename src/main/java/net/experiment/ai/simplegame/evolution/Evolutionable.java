package net.experiment.ai.simplegame.evolution;

import net.experiment.ai.simplegame.player.AutomatedPlayer;

public interface Evolutionable {

    void prepare() throws Exception;

    void play() throws Exception;

    AutomatedPlayer bestPlayer();

    void evolve() throws Exception;
}
