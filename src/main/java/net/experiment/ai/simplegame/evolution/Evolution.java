package net.experiment.ai.simplegame.evolution;

import net.experiment.ai.simplegame.brain.Brain;
import net.experiment.ai.simplegame.player.AIPlayer;
import net.experiment.ai.simplegame.player.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class Evolution {
    private Random random = new Random(System.currentTimeMillis());

    public List<Player> naturalSelection(List<Player> playerList, int nextPopulationSize) throws Exception {
        // let's select the N best players
        List<Player> bestPlayers = selectBestPlayers(playerList, 10);
        for (Player player : bestPlayers) {
            System.out.println("TOP -> " + player + " with fitness " + player.calculateFitness());
        }

        // then apply single point crossover algo (see http://accromath.uqam.ca/2019/10/algorithmes-genetiques/) to produce a new population
        List<Player> nextGeneration = new ArrayList<>();
        for (int i = 0; i < nextPopulationSize; i++) {
            nextGeneration.add(crossover(bestPlayers, i));
        }

        return nextGeneration;
    }

    private Player crossover(List<Player> bestPlayers, int rank) throws Exception {
        Player player1 = bestPlayers.get(random.nextInt(bestPlayers.size()));
        Player player2 = bestPlayers.get(random.nextInt(bestPlayers.size()));

        Brain newBrain;
        if (player1.toString().equals(player2.toString())) {
            newBrain = ((AIPlayer) player1).getBrain();
        } else {
            newBrain = crossover(((AIPlayer) player1).getBrain(), ((AIPlayer) player2).getBrain());
        }
        AIPlayer newPlayer = new AIPlayer(player1.getGameWorld(), "Player " + rank, ((AIPlayer) player1).getMaxMoves());
        newPlayer.setBrain(mutate(newBrain));

        return newPlayer;
    }

    private Brain mutate(Brain brain) {
        return brain.mutate(0.05);
    }

    private Brain crossover(Brain brain1, Brain brain2) {
        return brain1.crossover(brain2);
    }

    private List<Player> selectBestPlayers(List<Player> playerList, int howMany) {
        Map<Player, Double> playerToFitnessMap = playerList.stream()
                .collect(Collectors.toMap(p -> p, Player::calculateFitness));
        LinkedHashMap<Player, Double> topPlayerToFitnessMap = playerToFitnessMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(howMany)
                .collect(Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        return new ArrayList<>(topPlayerToFitnessMap.keySet());
    }
}
