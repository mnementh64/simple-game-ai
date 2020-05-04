package net.experiment.ai.simplegame.player;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import net.experiment.ai.simplegame.game.GameWorld;
import net.experiment.ai.simplegame.geometry.GameBoardPosition;

public class Player {
    // environment properties
    protected GameWorld gameWorld;
    protected GameBoardPosition position;

    // runtime properties
    private int score;
    private int nbMovesToFirstScore = 0;
    private boolean win = false;
    private int nbMoves = 0;
    protected int cumulativeNbMoves = 0;

    public Player(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
    }

    public void render(GraphicsContext gc, int width, int height, int tileSize) {
        double centerOfCellX = (position.colIndex + 0.5) * tileSize;
        double centerOfCellY = (position.rowIndex + 0.5) * tileSize;

        if (!win) {
            gc.setFill(Color.GREENYELLOW);
        } else {
            gc.setFill(Color.YELLOW);
        }
        gc.fillOval(centerOfCellX - 5, centerOfCellY - 5, 10, 10);
    }

    public void askedToMove(GameWorld.Direction direction) {
        nbMoves++;
        cumulativeNbMoves++;
    }

    public void up() {
        position.up();
    }

    public void down() {
        position.down();
    }

    public void left() {
        position.left();
    }

    public void right() {
        position.right();
    }

    /**
     * Reinint internal player properties for next level
     */
    public void reinit() {
        nbMoves = 0;
        nbMovesToFirstScore = 0;
        // do not reinit cumulativeNbMoves
        win = false;
    }

    public void setStartPosition(GameBoardPosition position) {
        this.position = position;
    }

    public GameBoardPosition getPosition() {
        return new GameBoardPosition(position.rowIndex, position.colIndex);
    }

    public void addToScore(int value) {
        if (score == 0) {
            nbMovesToFirstScore = nbMoves;
        }
        score += value;
    }

    public void win() {
        win = true;
    }

    public int getScore() {
        return score;
    }

    public boolean isWin() {
        return win;
    }

    public int getNbMoves() {
        return nbMoves;
    }

    public int getCumulativeNbMoves() {
        return cumulativeNbMoves;
    }

    protected String performanceToString() {
        return "score:" + score + ", " + nbMoves + " moves, first score after " + nbMovesToFirstScore + " moves, win:" + win;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public GameWorld getGameWorld() {
        return gameWorld;
    }

    public double calculateFitness() {
        return score * 5 + (win ? 1000 : 0) + (100 - nbMoves) + (100 - nbMovesToFirstScore) * 2;
    }
}
