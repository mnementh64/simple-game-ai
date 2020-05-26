package net.experiment.ai.simplegame.player;

import net.mnementh64.neural.Network;
import net.mnementh64.neural.model.activation.ActivationUtils;

public class DQLModel {

    private final Network network;

    public DQLModel() throws Exception {
        this.network = new Network.Builder()
                .addLayer(10, ActivationUtils.relu) // resize
                .addLayer(10, ActivationUtils.relu) //
                .addLayer(4, ActivationUtils.relu) // should be linear - Q Value for each direction
                .build();
    }

    public double[] predict(int[][] state) {
        return new double[4];
    }

    public void fit(int[][] state, double[] qValues) {

    }
}
