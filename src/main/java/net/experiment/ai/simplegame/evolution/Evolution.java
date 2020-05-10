package net.experiment.ai.simplegame.evolution;

import net.experiment.ai.simplegame.brain.Brain;
import net.experiment.ai.simplegame.brain.NNBrain;
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
    private static final int NB_BEST_PLAYERS = 200;
    private final Random random = new Random(System.currentTimeMillis());

    public List<Player> naturalSelection(List<Player> playerList, int nextPopulationSize) throws Exception {
        // let's select the N best players
        List<Player> bestPlayers = selectBestPlayers(playerList, NB_BEST_PLAYERS);
//        for (Player player : bestPlayers) {
//            System.out.println("TOP -> " + player + " with fitness " + player.calculateFitness());
//        }

        // then apply single point crossover algo (see http://accromath.uqam.ca/2019/10/algorithmes-genetiques/) to produce a new population
        List<Player> nextGeneration = new ArrayList<>();

        // keep all best players in next generation
        for (int i = 0; i < NB_BEST_PLAYERS; i++) {
            nextGeneration.add(clonePlayer(bestPlayers.get(i)));
        }
        // crossover those players to create offspring
        for (int i = 0; i < nextPopulationSize - NB_BEST_PLAYERS; i++) {
            nextGeneration.add(crossover(bestPlayers));
        }

//        // Offspring is only made of best player being randomly mutated
//        Player best = clonePlayer(bestPlayers.get(0));
//        for (int i = 0; i < nextPopulationSize; i++) {
//            Player clone = clonePlayer(best);
////            mutate((AIPlayer) clone, 0.01);
//            nextGeneration.add(clone);
//        }

        return nextGeneration;
    }

    private Player clonePlayer(Player player) throws Exception {
        AIPlayer clone = new AIPlayer(player.getGameWorld(), ((AIPlayer) player).getMaxMoves());
        NNBrain brain = new NNBrain(player.getGameWorld(), ((NNBrain) ((AIPlayer) player).getBrain()).getNeuralNetwork());
        clone.setBrain(brain);

        return clone;
    }

    private Player crossover(List<Player> bestPlayers) throws Exception {
        Player parent1 = bestPlayers.get(random.nextInt(bestPlayers.size()));
        Player parent2 = bestPlayers.get(random.nextInt(bestPlayers.size()));

        Brain childBrain;
        if (parent1.toString().equals(parent2.toString())) {
            childBrain = ((AIPlayer) parent1).getBrain();
        } else {
            childBrain = crossover(((AIPlayer) parent1).getBrain(), ((AIPlayer) parent2).getBrain()); // include mutation rate
            childBrain.mutate(0.01, true, -1.0, 1.0);
        }
        AIPlayer child = new AIPlayer(parent1.getGameWorld(), ((AIPlayer) parent1).getMaxMoves());
        child.setBrain(childBrain);

        return child;
    }

    private Brain crossover(Brain brain1, Brain brain2) {
        return brain1.crossover(brain2);
    }

    private void mutate(AIPlayer best, double mutationRate) {
        best.getBrain().mutate(mutationRate, true, -1.0, 1.0);
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
