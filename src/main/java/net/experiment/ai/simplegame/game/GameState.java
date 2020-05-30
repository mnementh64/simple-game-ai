package net.experiment.ai.simplegame.game;

import net.experiment.ai.simplegame.geometry.GameBoardPosition;

/**
 * Level matrix is represented as a 1-dimension array of cells.
 * Numbers are like :
 * <p>
 * 00 01 02 03 04 05 06 07
 * 08 09 10 ..
 * ..
 * .........59 60 61 62 63
 * </p>
 * <p>
 * with values = CELL_TYPE.getCode()
 */
public class GameState {

    private final int[] cells;

    public GameState(int[][] cells, GameBoardPosition playerPosition) {
        this.cells = new int[cells.length * cells[0].length];
        for (int row = 0; row < cells.length; row++) {
            for (int col = 0; col < cells[row].length; col++) {
                this.cells[row * 8 + col] = cells[row][col];
            }
        }
        this.cells[playerPosition.rowIndex * 8 + playerPosition.colIndex] = GameLevel.CELL_TYPE.PLAYER.code;
    }

    public int[] getCells() {
        return cells;
    }
}
