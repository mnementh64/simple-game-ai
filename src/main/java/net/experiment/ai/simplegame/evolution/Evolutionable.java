package net.experiment.ai.simplegame.evolution;

import net.experiment.ai.simplegame.player.AutomatedPlayer;
import net.experiment.ai.simplegame.player.Player;

import java.util.List;

public interface Evolutionable {
    List<AutomatedPlayer> naturalSelection(List<AutomatedPlayer> playerList, int nextPopulationSize) throws Exception;

    void prepare() throws Exception;

    void play();

    AutomatedPlayer bestPlayer();

    void evolve() throws Exception;
}
