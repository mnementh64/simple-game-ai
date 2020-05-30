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
        // we ignore first / last row / col (only walls)
        this.cells = new int[(cells.length - 2) * (cells[0].length - 2)];
        for (int row = 1; row < (cells.length - 1); row++) {
            for (int col = 1; col < (cells[row].length - 1); col++) {
                this.cells[indexFrom(row, col)] = cells[row][col];
            }
        }
        this.cells[indexFrom(playerPosition.rowIndex, playerPosition.colIndex)] = GameLevel.CELL_TYPE.PLAYER.code;
    }

    private int indexFrom(int row, int col) {
        return (row - 1) * 8 + (col - 1);
    }

    public int[] getCells() {
        return cells;
    }
}
