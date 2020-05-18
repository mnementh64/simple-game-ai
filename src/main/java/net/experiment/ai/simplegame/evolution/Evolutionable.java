package net.experiment.ai.simplegame.evolution;

import net.experiment.ai.simplegame.player.Player;

import java.util.List;

public interface Evolutionable {
    List<Player> naturalSelection(List<Player> playerList, int nextPopulationSize) throws Exception;
}
