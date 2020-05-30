package net.experiment.ai.simplegame.player;

import net.experiment.ai.simplegame.game.GameState;
import net.mnementh64.neural.Network;
import net.mnementh64.neural.model.activation.ActivationUtils;

public class DQLModel {

    private final Network network;

    public DQLModel(int stateSize) throws Exception {
        this.network = new Network.Builder()
                .addLayer(stateSize, ActivationUtils.relu)
                .addLayer(10, ActivationUtils.relu) //
                .addLayer(4, ActivationUtils.relu) // should be linear - Q Value for each direction
                .build();
    }

    public double[] predict(GameState state) {
        return new double[4];
    }

    public void fit(GameState state, double[] qValues) {

    }
}
