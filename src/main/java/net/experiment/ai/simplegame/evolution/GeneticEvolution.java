package net.experiment.ai.simplegame.evolution;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.experiment.ai.simplegame.brain.Brain;
import net.experiment.ai.simplegame.brain.PerceptronBrain;
import net.experiment.ai.simplegame.game.GameLevel;
import net.experiment.ai.simplegame.game.GameWorld;
import net.experiment.ai.simplegame.player.AutomatedPlayer;
import net.experiment.ai.simplegame.player.PerceptronBrainPlayer;
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
    private static final int NB_BEST_PLAYERS = 100;
    private static final int POPULATION_SIZE = 1500;

    private final Random random = new Random(System.currentTimeMillis());
    private final Map<Integer, Integer> playerIdToAgeMap = new HashMap<>();
    private final ObjectMapper mapper = new ObjectMapper();
    private final GameWorld gameWorld;
    private final int maxMoves;
    private final GameLevel gameLevel;
    private final boolean saveBestPlayer;

    private final List<AutomatedPlayer> playerList = new ArrayList<>();
    private int numGeneration = 1;
    private AutomatedPlayer bestPlayer;
    private int nbTimeMaximumReached = 0;

    public GeneticEvolution(GameWorld gameWorld, int maxMoves, GameLevel gameLevel, boolean saveBestPlayer) {
        this.gameWorld = gameWorld;
        this.maxMoves = maxMoves;
        this.gameLevel = gameLevel;
        this.saveBestPlayer = saveBestPlayer;
    }

    @Override
    public void prepare() throws Exception {
        // create a population of AI Players
        playerList.clear();
        for (int i = 0; i < POPULATION_SIZE; i++) {
            playerList.add(new PerceptronBrainPlayer(gameWorld, maxMoves));
        }
    }

    @Override
    public void play() {
        // repeat for each AI PLayer of the population
        double bestFitness = -1.0;
        for (AutomatedPlayer player : playerList) {
            gameWorld.init(player, gameLevel);
            for (int i = 0; i < maxMoves; i++) {
                boolean win = gameWorld.autoMovePlayer().win;
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
    public void evolve() throws Exception {
        if (bestPlayer.getScore() == gameLevel.bestScore()) {
            nbTimeMaximumReached++;

            if (nbTimeMaximumReached >= 2) {
                if (saveBestPlayer) {
                    savePlayer(bestPlayer);
                }
                System.exit(1);
            }
        }

        // create players for the next generation
        numGeneration++;
        System.out.println("*************************************************\nNew generation " + numGeneration);
        List<AutomatedPlayer> playersForNextGeneration = naturalSelection(playerList, POPULATION_SIZE);

        // activate this generation
        playerList.clear();
        playerList.addAll(playersForNextGeneration);

        // change diamonds positions for next play
        gameLevel.shufflePositions();
    }

    @Override
    public AutomatedPlayer bestPlayer() {
        return (AutomatedPlayer) bestPlayer;
    }

    private List<AutomatedPlayer> naturalSelection(List<AutomatedPlayer> playerList, int nextPopulationSize) throws Exception {
        // let's select the N best players
        List<AutomatedPlayer> bestPlayers = selectBestPlayers(playerList, NB_BEST_PLAYERS);

        // be sure all those best players have a registered age
        bestPlayers.forEach(player -> {
            if (!playerIdToAgeMap.containsKey(player.getId())) {
                playerIdToAgeMap.put(player.getId(), 1);
            }
        });

        // then apply single point crossover algo (see http://accromath.uqam.ca/2019/10/algorithmes-genetiques/) to produce a new population
        List<AutomatedPlayer> nextGeneration = new ArrayList<>();

        // keep all best players in next generation - remove those too old
        AutomatedPlayer clonePlayer = clonePlayer(bestPlayers.get(0));
        if (clonePlayer != null) {
            nextGeneration.add(clonePlayer);
            System.out.println("Best player " + bestPlayers.get(0).getId() + " (fitness=" + bestPlayers.get(0).calculateFitness() + ") becomes " + clonePlayer.getId());
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
            AutomatedPlayer childPlayer = crossover(selectParent(bestPlayers), selectParent(bestPlayers));
            mutate(childPlayer, 0.05);
            nextGeneration.add(childPlayer);
        }
        Collections.shuffle(nextGeneration);

        return nextGeneration;
    }

    private void savePlayer(AutomatedPlayer player) {
        String fileName = "/Users/sylvaincaillet/Downloads/player-" + player.getId() + ".json";
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(fileName), ((PerceptronBrain) ((AutomatedPlayer) player).getBrain()).getNeuralNetwork());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Roulette wheel selection of a parent
     */
    private AutomatedPlayer selectParent(List<AutomatedPlayer> bestPlayers) {
        double totalFitness = bestPlayers.stream().mapToDouble(Player::calculateFitness).sum();
        double randomFitness = random.nextDouble() * totalFitness;
        double cumulatedFitness = 0;
        for (AutomatedPlayer player : bestPlayers) {
            cumulatedFitness += player.calculateFitness();
            if (cumulatedFitness >= randomFitness) {
                return player;
            }
        }
        return null;
    }

    private AutomatedPlayer clonePlayer(AutomatedPlayer player) throws Exception {
        // this player is too old - skip it for next generation
        if (playerIdToAgeMap.get(player.getId()) >= MAX_AGE) {
            playerIdToAgeMap.remove(player.getId());
            return null;
        }

        AutomatedPlayer clone = new PerceptronBrainPlayer(player.getGameWorld(), player.getMaxMoves());
        PerceptronBrain brain = new PerceptronBrain(((PerceptronBrain) player.getBrain()).getNeuralNetwork());
        clone.setBrain(brain);

        // this clone's age is player's age plus 1
        playerIdToAgeMap.put(clone.getId(), playerIdToAgeMap.get(player.getId()) + 1);

        return clone;
    }

    private AutomatedPlayer crossover(AutomatedPlayer parent1, AutomatedPlayer parent2) throws Exception {
        if (parent1 == null || parent2 == null) {
            System.err.println("Parent null !");
            return null;
        }
        Brain childBrain;
        if (parent1.getId() == parent2.getId()) {
            childBrain = ((AutomatedPlayer) parent1).getBrain();
        } else {
            childBrain = crossover(parent1.getBrain(), parent2.getBrain()); // include mutation rate
        }
        AutomatedPlayer child = new PerceptronBrainPlayer(parent1.getGameWorld(), parent1.getMaxMoves());
        child.setBrain(childBrain);

        return child;
    }

    private Brain crossover(Brain brain1, Brain brain2) throws Exception {
        return brain1.crossover(brain2);
    }

    private void mutate(AutomatedPlayer best, double mutationRate) {
        best.getBrain().mutate(mutationRate, true, -1.0, 1.0);
    }

    private List<AutomatedPlayer> selectBestPlayers(List<AutomatedPlayer> playerList, int howMany) {
        Map<AutomatedPlayer, Double> playerToFitnessMap = playerList.stream()
                .collect(Collectors.toMap(p -> p, Player::calculateFitness));
        LinkedHashMap<AutomatedPlayer, Double> topPlayerToFitnessMap = playerToFitnessMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(howMany)
                .collect(Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        return new ArrayList<>(topPlayerToFitnessMap.keySet());
    }
}
