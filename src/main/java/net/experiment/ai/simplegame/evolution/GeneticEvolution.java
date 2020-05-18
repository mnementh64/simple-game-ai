package net.experiment.ai.simplegame.evolution;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.experiment.ai.simplegame.brain.Brain;
import net.experiment.ai.simplegame.brain.NNBrain;
import net.experiment.ai.simplegame.game.GameLevel;
import net.experiment.ai.simplegame.game.GameWorld;
import net.experiment.ai.simplegame.player.AIPlayer;
import net.experiment.ai.simplegame.player.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class GeneticEvolution implements Evolutionable {
    private static final int MAX_AGE = 15;
    private static final int NB_BEST_PLAYERS = 200;
    private static final int POPULATION_SIZE = 1000;

    private final Random random = new Random(System.currentTimeMillis());
    private final Map<Integer, Integer> playerIdToAgeMap = new HashMap<>();
    private final ObjectMapper mapper = new ObjectMapper();
    private final GameWorld gameWorld;
    private final int maxMoves;
    private final GameLevel gameLevel;
    private final boolean saveBestPlayer;

    private List<Player> playerList = new ArrayList<>();
    private int numGeneration = 1;
    private Player bestPlayer;

    public GeneticEvolution(GameWorld gameWorld, int maxMoves, GameLevel gameLevel, boolean saveBestPlayer) {
        this.gameWorld = gameWorld;
        this.maxMoves = maxMoves;
        this.gameLevel = gameLevel;
        this.saveBestPlayer = saveBestPlayer;
    }

    @Override
    public List<Player> naturalSelection(List<Player> playerList, int nextPopulationSize) throws Exception {
        // let's select the N best players
        List<Player> bestPlayers = selectBestPlayers(playerList, NB_BEST_PLAYERS);

        // be sure all those best players have a registered age
        bestPlayers.forEach(player -> {
            if (!playerIdToAgeMap.containsKey(player.getId())) {
                playerIdToAgeMap.put(player.getId(), 1);
            }
        });

        // then apply single point crossover algo (see http://accromath.uqam.ca/2019/10/algorithmes-genetiques/) to produce a new population
        List<Player> nextGeneration = new ArrayList<>();

        // keep all best players in next generation - remove those too old
        Player clonePlayer = clonePlayer(bestPlayers.get(0));
        if (clonePlayer != null) {
            nextGeneration.add(clonePlayer);
            System.out.println("Best player " + bestPlayers.get(0).getId() + " (fitness=" + bestPlayers.get(0).calculateFitness() + ") becomes " + clonePlayer.getId());
            if (saveBestPlayer) {
                savePlayer(bestPlayers.get(0));
            }
        }
        for (int i = 1; i < NB_BEST_PLAYERS; i++) {
            clonePlayer = clonePlayer(bestPlayers.get(i));
            if (clonePlayer != null) {
                nextGeneration.add(clonePlayer);
            }
        }
        Collections.shuffle(nextGeneration);
        System.out.println("Next generation with " + nextGeneration.size() + " cloned players and " + (nextPopulationSize - nextGeneration.size()) + " X children");

        // crossover those players to create offspring
        for (int i = 0; i < nextPopulationSize - nextGeneration.size(); i++) {
            Player childPlayer = crossover(selectParent(bestPlayers), selectParent(bestPlayers));
            mutate((AIPlayer) childPlayer, 0.01);
            nextGeneration.add(childPlayer);
        }
        Collections.shuffle(nextGeneration);

        return nextGeneration;
    }

    @Override
    public void prepare() throws Exception {
        // create a population of AI Players
        playerList.clear();
        for (int i = 0; i < POPULATION_SIZE; i++) {
            playerList.add(new AIPlayer(gameWorld, maxMoves));
        }
    }

    @Override
    public void play() {
        // repeat for each AI PLayer of the population
        double bestFitness = -1.0;
        for (Player player : playerList) {
            gameWorld.init(player, gameLevel);
            for (int i = 0; i < maxMoves; i++) {
                boolean win = gameWorld.autoMovePlayer();
                if (win) {
                    break;
                }
            }
            double fitness = player.calculateFitness();
            if (fitness > bestFitness) {
                bestFitness = fitness;
                bestPlayer = player;
            }
        }
    }

    @Override
    public AIPlayer bestPlayer() {
        return (AIPlayer) bestPlayer;
    }

    @Override
    public void evolve() throws Exception {
        if (gameLevel.isCompleted()) {
            System.exit(1);
        }

        // create players for the next generation
        numGeneration++;
        System.out.println("*************************************************\nNew generation " + numGeneration);
        List<Player> playersForNextGeneration = naturalSelection(playerList, POPULATION_SIZE);

        // activate this generation
        playerList.clear();
        playerList.addAll(playersForNextGeneration);

        // change diamonds positions for next play
        gameLevel.shufflePositions();
    }

    private void savePlayer(Player player) {
        String fileName = "/Users/sylvaincaillet/Downloads/player-" + player.getId() + ".json";
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(fileName), ((NNBrain) ((AIPlayer) player).getBrain()).getNeuralNetwork());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Roulette wheel selection of a parent
     */
    private Player selectParent(List<Player> bestPlayers) {
        double totalFitness = bestPlayers.stream().mapToDouble(Player::calculateFitness).sum();
        double randomFitness = random.nextDouble() * totalFitness;
        double cumulatedFitness = 0;
        for (Player player : bestPlayers) {
            cumulatedFitness += player.calculateFitness();
            if (cumulatedFitness >= randomFitness) {
                return player;
            }
        }
        return null;
    }

    private Player clonePlayer(Player player) throws Exception {
        // this player is too old - skip it for next generation
        if (playerIdToAgeMap.get(player.getId()) >= MAX_AGE) {
            playerIdToAgeMap.remove(player.getId());
            return null;
        }

        AIPlayer clone = new AIPlayer(player.getGameWorld(), ((AIPlayer) player).getMaxMoves());
        NNBrain brain = new NNBrain(player.getGameWorld(), ((NNBrain) ((AIPlayer) player).getBrain()).getNeuralNetwork());
        clone.setBrain(brain);

        // this clone's age is player's age plus 1
        playerIdToAgeMap.put(clone.getId(), playerIdToAgeMap.get(player.getId()) + 1);

        return clone;
    }

    private Player crossover(Player parent1, Player parent2) throws Exception {
        if (parent1 == null || parent2 == null) {
            System.err.println("Parent null !");
            return null;
        }
        Brain childBrain;
        if (parent1.getId() == parent2.getId()) {
            childBrain = ((AIPlayer) parent1).getBrain();
        } else {
            childBrain = crossover(((AIPlayer) parent1).getBrain(), ((AIPlayer) parent2).getBrain()); // include mutation rate
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
