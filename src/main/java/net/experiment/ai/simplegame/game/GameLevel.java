package net.experiment.ai.simplegame.game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import net.experiment.ai.simplegame.geometry.GameBoardPosition;
import net.experiment.ai.simplegame.geometry.Vector;
import net.experiment.ai.simplegame.player.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GameLevel {
    private static final int NB_COLUMNS = 10;
    private static final int NB_ROWS = 10;
    public static final int TILE_SIZE = 20;

    public enum CELL_TYPE {
        EMPTY(0),
        WALL(1),
        DIAMOND(2),
        PLAYER(3);

        public final int code;


        CELL_TYPE(int code) {
            this.code = code;
        }

        public CELL_TYPE getByCode(int code) {
            for (CELL_TYPE type : CELL_TYPE.values()) {
                if (type.code == code) {
                    return type;
                }
            }
            return null;
        }

    }

    public static final GameLevel LEVEL_1 = new GameLevel(
            "" +
                    "WWWWWWWWWW" +
                    "W........W" +
                    "W........W" +
                    "W........W" +
                    "W........W" +
                    "W........W" +
                    "W........W" +
                    "W........W" +
                    "W........W" +
                    "WWWWWWWWWW"
            , 11
    );
    //    public static final GameLevel LEVEL_1 = new GameLevel(
//            "" +
//                    "WWWWWWWWWWWWWWWWWWWW" +
//                    "W..................W" +
//                    "W..................W" +
//                    "W..................W" +
//                    "W..................W" +
//                    "W..................W" +
//                    "W..................W" +
//                    "W..................W" +
//                    "W..................W" +
//                    "W..................W" +
//                    "W..................W" +
//                    "W..................W" +
//                    "W..................W" +
//                    "W..................W" +
//                    "W..................W" +
//                    "W..................W" +
//                    "W..................W" +
//                    "W..................W" +
//                    "W..................W" +
//                    "WWWWWWWWWWWWWWWWWWWW"
//            , 11
//    );
    public static final GameLevel LEVEL_2 = new GameLevel(
            "" +
                    "WWWWWWWWWW" +
                    "W.....W..W" +
                    "W.....W..W" +
                    "W.....W..W" +
                    "W...WWWW.W" +
                    "W........W" +
                    "W.....W..W" +
                    "W...WWW..W" +
                    "W........W" +
                    "WWWWWWWWWW"
            , 7
    );
//    public static final GameLevel LEVEL_2 = new GameLevel(
//            "" +
//                    "WWWWWWWWWWWWWWWWWWWW" +
//                    "W.......W..........W" +
//                    "W.......W..........W" +
//                    "W..................W" +
//                    "W.......W..........W" +
//                    "W.......W..........W" +
//                    "W...WWWWWWWWW......W" +
//                    "W..................W" +
//                    "W..................W" +
//                    "W..................W" +
//                    "W...........W......W" +
//                    "W...........W......W" +
//                    "W...........WWW....W" +
//                    "W..................W" +
//                    "W..................W" +
//                    "W..................W" +
//                    "W..................W" +
//                    "W..................W" +
//                    "W..................W" +
//                    "WWWWWWWWWWWWWWWWWWWW"
//            , 7
//    );

    private final String levelAsString;
    private final int[][] levelMatrix;
    private final int nbDiamonds;
    private GameBoardPosition startPosition;
    private List<GameBoardPosition> diamondPositions = new ArrayList<>();

    private int nbDiamondsFound = 0;

    public GameLevel(String levelAsString, int nbDiamonds) throws IllegalArgumentException {
        this.levelAsString = levelAsString;
        this.levelMatrix = new int[NB_COLUMNS][NB_ROWS];
        this.nbDiamonds = nbDiamonds;

        if (levelAsString.length() != NB_COLUMNS * NB_ROWS) {
            throw new IllegalArgumentException("Level definition doesn't fit in size " + NB_ROWS + "*" + NB_COLUMNS);
        }
        initLevelMatrix(levelAsString);
        shufflePositions();
    }

    public void reinit() {
        initLevelMatrix(levelAsString);
        nbDiamondsFound = 0;
    }

    private void initLevelMatrix(String levelAsString) {
        int rowIndex = 0;
        int colIndex = 0;
        for (char cell : levelAsString.toCharArray()) {
            switch (cell) {
                case 'W':
                    levelMatrix[rowIndex][colIndex] = CELL_TYPE.WALL.code;
                    break;
                default:
                    levelMatrix[rowIndex][colIndex] = CELL_TYPE.EMPTY.code;
                    break;
            }

            colIndex++;
            if (colIndex >= NB_COLUMNS) {
                colIndex = 0;
                rowIndex++;
            }
        }

        // set diamonds
        diamondPositions.forEach(pos -> levelMatrix[pos.rowIndex][pos.colIndex] = CELL_TYPE.DIAMOND.code);
    }

    public void shufflePositions() {
        Random random = new Random(System.currentTimeMillis());
        List<GameBoardPosition> possiblePositions = new ArrayList<>();
        for (int row = 1; row < NB_ROWS - 1; row++) {
            for (int col = 1; col < NB_COLUMNS - 1; col++) {
                // wall position is not allowed for player / diamond
                if (levelMatrix[row][col] == CELL_TYPE.WALL.code) {
                    continue;
                }
                possiblePositions.add(new GameBoardPosition(row, col));
            }
        }
        Collections.shuffle(possiblePositions);

        // random player start position
        this.startPosition = possiblePositions.remove(random.nextInt(possiblePositions.size()));

        // random diamonds positions
        diamondPositions.clear();
        for (int i = 0; i < nbDiamonds; i++) {
            // pick-up a random available position
            GameBoardPosition position = possiblePositions.remove(random.nextInt(possiblePositions.size()));
            diamondPositions.add(position);
        }
    }

    /**
     * Each tile of the game level must be rendered accordingly to the available width / height
     */
    public void render(GraphicsContext gc, int width, int height) {
        // Clear the graphic area
        gc.setFill(Color.WHITE);
        gc.clearRect(0, 0, width, height);

        for (int rowIndex = 0; rowIndex < NB_ROWS; rowIndex++) {
            for (int colIndex = 0; colIndex < NB_COLUMNS; colIndex++) {
                if (levelMatrix[rowIndex][colIndex] == CELL_TYPE.WALL.code) {
                    renderWall(gc, rowIndex, colIndex);
                } else if (levelMatrix[rowIndex][colIndex] == CELL_TYPE.EMPTY.code) {
                    renderEmpty(gc, rowIndex, colIndex);
                } else if (levelMatrix[rowIndex][colIndex] == CELL_TYPE.DIAMOND.code) {
                    renderDiamond(gc, rowIndex, colIndex);
                }
                renderStartPosition(gc, startPosition);
            }
        }
    }

    public int manageConsequences(Player player) {
        int reward = 0;

        // found diamond ?
        GameBoardPosition position = player.getPosition();
        if (levelMatrix[position.rowIndex][position.colIndex] == CELL_TYPE.DIAMOND.code) {
            levelMatrix[position.rowIndex][position.colIndex] = CELL_TYPE.EMPTY.code;
            nbDiamondsFound++;
            reward = 100;
            player.addToScore(100);

            if (nbDiamondsFound == nbDiamonds) {
                player.win();
            }
        }

        return reward;
    }

    private void renderStartPosition(GraphicsContext gc, GameBoardPosition startPosition) {
        gc.setFill(Color.ORANGE);
        gc.fillRect(startPosition.colIndex * TILE_SIZE, startPosition.rowIndex * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        gc.setStroke(Color.YELLOW);
        gc.strokeRect(startPosition.colIndex * TILE_SIZE, startPosition.rowIndex * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }

    private void renderDiamond(GraphicsContext gc, int rowIndex, int colIndex) {
        gc.setFill(Color.RED);
        gc.fillRect((colIndex + 0.5) * TILE_SIZE + -4, (rowIndex + 0.5) * TILE_SIZE - 4, 8, 8);
    }

    private void renderEmpty(GraphicsContext gc, int rowIndex, int colIndex) {
        gc.setStroke(Color.LIGHTGRAY);
        gc.strokeRect(colIndex * TILE_SIZE, rowIndex * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }

    private void renderWall(GraphicsContext gc, int rowIndex, int colIndex) {
//        gc.setFill(Color.DARKGRAY);
//        gc.fillRect(colIndex * TILE_SIZE, rowIndex * TILE_SIZE, TILE_SIZE, TILE_SIZE);
//        gc.setStroke(Color.LIGHTGRAY);
//        gc.strokeRect(colIndex * TILE_SIZE, rowIndex * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        gc.drawImage(GameImage.WALL.getImage(), colIndex * TILE_SIZE, rowIndex * TILE_SIZE);
    }

    public GameBoardPosition getStartPosition() {
        return new GameBoardPosition(startPosition.rowIndex, startPosition.colIndex);
    }

    public int getTileSize() {
        return TILE_SIZE;
    }

    public boolean allowPositionToPlayer(GameBoardPosition position) {
        int cellCode = levelMatrix[position.rowIndex][position.colIndex];
        return cellCode != CELL_TYPE.WALL.code;
    }

    public int getNumberOfRows() {
        return NB_ROWS;
    }

    public int bestScore() {
        return nbDiamonds * 100;
    }

    /**
     * For each direction in the following order, return some values.
     * <p>
     * S - SE - E - NE - N - NW - W - SW
     * because y is aimed to the south in our coordinates system (java canvas !)
     * </p>
     */
    public double[] lookInAllDirections(GameBoardPosition position) {
        List<Vector> allDirections = Arrays.asList(
                new Vector(1, 0),
                new Vector(1, 1),
                new Vector(0, 1),
                new Vector(-1, 1),
                new Vector(-1, 0),
                new Vector(-1, -1),
                new Vector(0, -1),
                new Vector(1, -1)
        );

        int index = 0;
        double[] globalVision = new double[16]; // 2 distances per direction
        for (Vector directionToLook : allDirections) {
            double[] vision = lookInDirection(directionToLook, position);

            globalVision[index++] = vision[0];
            globalVision[index++] = vision[1];
        }

        return globalVision;
    }

    /**
     * - 1/distance to a wall
     * - 1/distance to a diamond : 0 means no diamond
     */
    private double[] lookInDirection(Vector directionToLook, GameBoardPosition playerPosition) {
        double[] vision = new double[3];

        // loop until we find a wall (which is expected !)
        // start by moving
        GameBoardPosition position = playerPosition.moveTo(directionToLook);

        int distance = 1;
        int distanceDiamond = 0;
        boolean diamondFound = false;
        while (!isWall(position)) {
            if (!diamondFound && isDiamond(position)) {
                diamondFound = true;
                distanceDiamond = distance;
            }
            position = position.moveTo(directionToLook);
            distance++;
        }

        vision[0] = 1.0 / distance;
        vision[1] = distanceDiamond == 0 ? 0 : 1.0 / distanceDiamond;

        return vision;
    }

    private boolean isWall(GameBoardPosition position) {
        int cellType = levelMatrix[position.rowIndex][position.colIndex];
        return cellType == CELL_TYPE.WALL.code;
    }

    private boolean isDiamond(GameBoardPosition position) {
        return levelMatrix[position.rowIndex][position.colIndex] == CELL_TYPE.DIAMOND.code;
    }

    public int[][] getLevelMatrix() {
        return levelMatrix;
    }

    public int getStateSize() {
        return (NB_COLUMNS - 2) * (NB_ROWS - 2); // ignore walls on the edges
    }
}
