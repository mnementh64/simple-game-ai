package net.experiment.ai.simplegame;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Player {
    private GameBoardPosition position;
    private int score;

    public Player(GameBoardPosition startPosition) {
        this.position = startPosition;
    }

    public void render(GraphicsContext gc, int width, int height, int tileSize) {
        double centerOfCellX = (position.colIndex + 0.5) * tileSize;
        double centerOfCellY = (position.rowIndex + 0.5) * tileSize;

        gc.setFill(Color.GREENYELLOW);
        gc.fillOval(centerOfCellX - 5, centerOfCellY - 5, 10, 10);
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

    public GameBoardPosition getPosition() {
        return new GameBoardPosition(position.rowIndex, position.colIndex);
    }
}
