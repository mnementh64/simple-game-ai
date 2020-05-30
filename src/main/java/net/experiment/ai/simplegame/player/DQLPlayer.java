package net.experiment.ai.simplegame.player;

import net.experiment.ai.simplegame.evolution.DQLMemoryItem;
import net.experiment.ai.simplegame.game.GameState;
import net.experiment.ai.simplegame.game.GameWorld;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Heavily inspired by https://github.com/gsurma/cartpole/blob/master/cartpole.py
 */
public class DQLPlayer extends AutomatedPlayer {
    private static final int MEMORY_SIZE = 1000000;
    private static final int BATCH_SIZE = 20;

    private static final double EXPLORATION_MAX = 1.0;
    private static final double EXPLORATION_MIN = 0.01;
    private static final double EXPLORATION_DECAY = 0.995;

    private static final double GAMMA = 0.95;
    private static final double LEARNING_RATE = 0.001;

    private static final Random RANDOM = new Random(System.currentTimeMillis());

    private final List<DQLMemoryItem> memory = new ArrayList<>(MEMORY_SIZE);
    private DQLModel model;
    private double explorationRate;

    public DQLPlayer(GameWorld gameWorld, int maxMoves) throws Exception {
        super(gameWorld, maxMoves);

        this.explorationRate = EXPLORATION_MAX;
        this.model = new DQLModel(gameWorld.getStateSize());
    }

    @Override
    public GameWorld.Direction computeNextMove() {
        // random move ?
        if (RANDOM.nextDouble() < explorationRate) {
            int value = RANDOM.nextInt(4);
            switch (value) {
                case 0:
                    return GameWorld.Direction.DOWN;
                case 1:
                    return GameWorld.Direction.UP;
                case 2:
                    return GameWorld.Direction.LEFT;
                case 3:
                    return GameWorld.Direction.RIGHT;
            }
            return GameWorld.Direction.NONE;
        } else {
            // else prediction from the model -> pick-up best proposition
            return bestDirection(model.predict(gameWorld.state()));
        }
    }

    public void remember(GameState state, int direction, int reward, GameState stateNext, boolean win) {
        memory.add(new DQLMemoryItem(state, direction, reward, stateNext, win));
        if (memory.size() > MEMORY_SIZE) {
            memory.remove(0); // remove oldest in case of memory overflow
        }
    }

    public void experienceReplay() {
        // not enough memory yet
        if (memory.size() < BATCH_SIZE) {
            return;
        }

        List<DQLMemoryItem> memoryBatch = pickupRandomly(memory, BATCH_SIZE);
        for (DQLMemoryItem memoryItem : memoryBatch) {
            double qUpdate = memoryItem.reward;
            if (!memoryItem.win) {
                qUpdate = memoryItem.reward + GAMMA * max(model.predict(memoryItem.stateNext));
            }

            double[] qValues = model.predict(memoryItem.state);
            qValues[memoryItem.direction] = qUpdate;

            model.fit(memoryItem.state, qValues);
        }

        explorationRate *= EXPLORATION_DECAY;
        explorationRate = Math.max(EXPLORATION_MIN, explorationRate);
    }

    private double max(double[] values) {
        double maxValue = -1.0;
        for (double value : values) {
            if (value > maxValue) {
                maxValue = value;
            }
        }
        return maxValue;
    }

    private List<DQLMemoryItem> pickupRandomly(List<DQLMemoryItem> memory, int nbItems) {
        List<Integer> randomIndexes = IntStream.range(0, memory.size()).boxed().collect(Collectors.toList());
        Collections.shuffle(randomIndexes);
        randomIndexes = randomIndexes.subList(0, nbItems);

        List<DQLMemoryItem> randomItems = new ArrayList<>();
        for (int i = 0; i < nbItems; i++) {
            DQLMemoryItem itemModel = memory.get(randomIndexes.get(i));
            randomItems.add(new DQLMemoryItem(itemModel));
        }

        return randomItems;
    }

    private GameWorld.Direction bestDirection(double[] predictions) {
        int indexMaxValue = 0;
        double maxValue = -1.0;
        for (int i = 0; i < predictions.length; i++) {
            if (predictions[i] > maxValue) {
                maxValue = predictions[i];
                indexMaxValue = i;
            }
        }
        return GameWorld.Direction.byCode(indexMaxValue);
    }
}
