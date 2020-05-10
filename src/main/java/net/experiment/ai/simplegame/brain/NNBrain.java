package net.experiment.ai.simplegame.brain;

import net.experiment.ai.simplegame.game.GameWorld;
import net.experiment.ai.simplegame.geometry.GameBoardPosition;
import net.mnementh64.neural.Network;
import net.mnementh64.neural.model.activation.ActivationUtils;
import net.mnementh64.neural.model.weight.WeightUtils;

import java.util.ArrayList;
import java.util.List;

public class NNBrain extends Brain {

    private Network neuralNetwork;

    public NNBrain(GameWorld gameWorld) throws Exception {
        super(gameWorld);
        this.neuralNetwork = new Network.Builder()
                .setWeightInitFunction(WeightUtils.gaussianNormalizedFunction)
                .addLayer(24, ActivationUtils.identity)
                .addLayer(18, ActivationUtils.relu)
                .addLayer(18, ActivationUtils.relu)
                .addLayer(4, ActivationUtils.sigmoid) // U,D,L,R
                .build();
    }

    public NNBrain(GameWorld gameWorld, Network network) {
        super(gameWorld);
        this.neuralNetwork = network;
        this.neuralNetwork.clearNodes();    // ensure this network is like a newly created network
    }

    @Override
    public GameWorld.Direction computeNextMove(double[] visualInformations, GameBoardPosition position) throws Exception {

        List<Double> decision = neuralNetwork.feedForward(doubleArrayToList(visualInformations));

        // interpret output in terms of direction (max value if the choosen direction)
        int maxIndex = 0;
        double max = 0;
        for (int i = 0; i < decision.size(); i++) {
            if (decision.get(i) > max) {
                max = decision.get(i);
                maxIndex = i;
            }
        }

        switch (maxIndex) {
            case 0:
                return GameWorld.Direction.UP;
            case 1:
                return GameWorld.Direction.DOWN;
            case 2:
                return GameWorld.Direction.LEFT;
            case 3:
                return GameWorld.Direction.RIGHT;
        }
        throw new IllegalAccessException("should not reach this code !");
    }

    @Override
    public Brain crossover(Brain brain2) {
        NNBrain nnBrain2 = (NNBrain) brain2;
        Network child = this.neuralNetwork.crossover(nnBrain2.neuralNetwork);

        return new NNBrain(this.gameWorld, child);
    }

    @Override
    public void mutate(double mutationRate, boolean applyExtremValue, double minValue, double maxValue) {
        neuralNetwork.mutate(mutationRate, applyExtremValue, minValue, maxValue);
    }

    private List<Double> doubleArrayToList(double[] visualInformations) {
        List<Double> values = new ArrayList<>();
        for (double d : visualInformations) {
            values.add(d);
        }
        return values;
    }

    public Network getNeuralNetwork() {
        return neuralNetwork;
    }
}
