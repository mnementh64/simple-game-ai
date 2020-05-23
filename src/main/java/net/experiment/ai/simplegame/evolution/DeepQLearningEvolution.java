package net.experiment.ai.simplegame.evolution;

import net.experiment.ai.simplegame.game.GameLevel;
import net.experiment.ai.simplegame.game.GameWorld;
import net.experiment.ai.simplegame.player.AutomatedPlayer;
import net.experiment.ai.simplegame.player.DQLearningSolver;
import net.experiment.ai.simplegame.player.MoveConsequences;

public class DeepQLearningEvolution implements Evolutionable {

    private final GameWorld gameWorld;
    private final int maxMoves;
    private final GameLevel gameLevel;
    private final boolean saveBestPlayer;

    private AutomatedPlayer player;

    public DeepQLearningEvolution(GameWorld gameWorld, int maxMoves, GameLevel gameLevel, boolean saveBestPlayer) {
        this.gameWorld = gameWorld;
        this.maxMoves = maxMoves;
        this.gameLevel = gameLevel;
        this.saveBestPlayer = saveBestPlayer;
    }

    @Override
    public void prepare() {
        player = new DQLearningSolver(gameWorld, maxMoves);
    }

    /**
     * One episode = agent plays until either :
     * - the game is won
     * - the number of moves is over
     */
    @Override
    public void play() {
        gameWorld.init(player, gameLevel);
        for (int i = 0; i < maxMoves; i++) {
            MoveConsequences moveConsequences = gameWorld.autoMovePlayer();
            if (moveConsequences.win) {
                break;
            }
        }


//        env = gym.make("CartPole-v1")
//        observation_space = env.observation_space.shape[0]
//        action_space = env.action_space.n
//        dqn_solver = DQNSolver(observation_space, action_space)
//        while True:
//        state = env.reset()
//        state = np.reshape(state, [1, observation_space])
//        while True:
//        env.render()
//        action = dqn_solver.act(state)
//        state_next, reward, terminal, info = env.step(action)
//        reward = reward if not terminal else -reward
//        state_next = np.reshape(state_next, [1, observation_space])
//        dqn_solver.remember(state, action, reward, state_next, terminal)
//        dqn_solver.experience_replay()
//        state = state_next
//        if terminal:
//        break
    }

    @Override
    public void evolve() throws Exception {

    }

    @Override
    public AutomatedPlayer bestPlayer() {
        return null;
    }
}
