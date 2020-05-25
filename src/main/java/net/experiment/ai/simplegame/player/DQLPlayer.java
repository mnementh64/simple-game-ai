package net.experiment.ai.simplegame.player;

import net.experiment.ai.simplegame.evolution.DQLMemoryItem;
import net.experiment.ai.simplegame.game.GameWorld;
import net.mnementh64.neural.Network;
import net.mnementh64.neural.model.activation.ActivationUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DQLPlayer extends AutomatedPlayer {
    private static final int MEMORY_SIZE = 1000000;
    private static final int BATCH_SIZE = 20;

    private static final double EXPLORATION_MAX = 1.0;
    private static final double EXPLORATION_MIN = 0.01;
    private static final double EXPLORATION_DECAY = 0.995;
    private static final Random RANDOM = new Random(System.currentTimeMillis());

    private final List<DQLMemoryItem> memory = new ArrayList<>(MEMORY_SIZE);
    private final Network model;
    private double explorationRate;

    public DQLPlayer(GameWorld gameWorld, int maxMoves) throws Exception {
        super(gameWorld, maxMoves);

        this.explorationRate = EXPLORATION_MAX;
        this.model = new Network.Builder()
                .addLayer(10, ActivationUtils.relu) // resize
                .addLayer(10, ActivationUtils.relu) //
                .addLayer(4, ActivationUtils.relu) // should be linear
                .build();
    }

    @Override
    public GameWorld.Direction computeNextMove() {
        // if random < explorationRate -> random move
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

        // else prediction from the model -> pick-up best proposition
//        output = model.feedForward(state)
    }

    public void remember(int[][] state, int direction, int reward, int[][] stateNext, boolean win) {
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

        List<DQLMemoryItem> memoryBatch = extractRandomly(memory, BATCH_SIZE);
        for (DQLMemoryItem memoryItem : memoryBatch) {
//        q_update = reward
//        if not terminal:
//        q_update = (reward + GAMMA * np.amax(self.model.predict(state_next)[0]))
//        q_values = self.model.predict(state)
//        q_values[0][action] = q_update
//        self.model.fit(state, q_values, verbose=0)
        }

        explorationRate *= EXPLORATION_DECAY;
        explorationRate = Math.max(EXPLORATION_MIN, explorationRate);
    }

    private List<DQLMemoryItem> extractRandomly(List<DQLMemoryItem> memory, int nbItems) {
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
}
