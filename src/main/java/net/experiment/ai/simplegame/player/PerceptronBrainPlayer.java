package net.experiment.ai.simplegame.player;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.experiment.ai.simplegame.brain.PerceptronBrain;
import net.experiment.ai.simplegame.game.GameWorld;
import net.mnementh64.neural.Network;

import java.io.File;
import java.io.IOException;

public class PerceptronBrainPlayer extends AutomatedPlayer {

    public PerceptronBrainPlayer(GameWorld gameWorld, int maxMoves) throws Exception {
        super(gameWorld, maxMoves, new PerceptronBrain());
    }

    public PerceptronBrainPlayer(GameWorld gameWorld, int maxMoves, String brainFileName) throws IOException {
        super(gameWorld, maxMoves, loadFromFile(brainFileName));
    }

    private static PerceptronBrain loadFromFile(String fileName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Network network = mapper.readValue(new File("/Users/sylvaincaillet/Downloads/player-33083.json"), Network.class);
        return new PerceptronBrain(network);
    }
}
