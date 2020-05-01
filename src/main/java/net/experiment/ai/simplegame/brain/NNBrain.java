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
        neuralNetwork = new Network.Builder()
                .setWeightInitFunction(WeightUtils.gaussianNormalizedFunction)
                .addLayer(24)
                .addLayer(18, ActivationUtils.relu)
                .addLayer(18, ActivationUtils.relu)
                .addLayer(4, ActivationUtils.sigmoid) // U,D,L,R
                .build();
    }

    @Override
    public GameWorld.Direction computeNextMove(double[] visualInformations, GameBoardPosition position) throws Exception {

        List<Double> decision = neuralNetwork.feedForward(doubleArrayToList(visualInformations));

        // TODO : interpret output in terms of direction
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

    private List<Double> doubleArrayToList(double[] visualInformations) {
        List<Double> values = new ArrayList<>();
        for (double d : visualInformations) {
            values.add(d);
        }
        return values;
    }


}
