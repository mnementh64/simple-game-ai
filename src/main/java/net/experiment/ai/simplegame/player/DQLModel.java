package net.experiment.ai.simplegame.player;

import net.experiment.ai.simplegame.game.GameState;
import net.mnementh64.neural.Network;
import net.mnementh64.neural.model.activation.ActivationUtils;
import net.mnementh64.neural.model.weight.WeightUtils;

import java.util.ArrayList;
import java.util.List;

public class DQLModel {

    private final Network network;

    public DQLModel(int stateSize, double learningRate) throws Exception {
        this.network = new Network.Builder()
                .setWeightInitFunction(WeightUtils.gaussianNormalizedFunction)
                .setLearningRate((float) learningRate)
                .addLayer(stateSize, ActivationUtils.relu)
                .addLayer(10, ActivationUtils.relu) //
                .addLayer(5, ActivationUtils.relu) // should be linear - Q Value for each direction
                .build();
    }

    public double[] predict(GameState state) throws Exception {
        List<Double> output = network.feedForward(doubleFrom(state));
        return output.stream().mapToDouble(Double::doubleValue).toArray();
    }

    private List<Double> doubleFrom(GameState state) {
        List<Double> values = new ArrayList<>();
        for (int val : state.getCells()) {
            values.add((double) val);
        }
        return values;
    }

    private List<Double> doubleFrom(double[] dValues) {
        List<Double> values = new ArrayList<>();
        for (double val : dValues) {
            values.add(val);
        }
        return values;
    }

    public void fit(double[] qValues) throws Exception {
        network.retroPropagateError(doubleFrom(qValues));
    }
}
