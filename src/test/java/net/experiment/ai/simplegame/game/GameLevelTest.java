package net.experiment.ai.simplegame.game;

import net.experiment.ai.simplegame.geometry.GameBoardPosition;
import org.junit.Assert;
import org.junit.Before;

public class GameLevelTest {

    private GameLevel level;

    @Before
    public void setUp() {
        level = new GameLevel(
                "" +
                        "WWWWWWWWWWWWWWOWWWWW" +
                        "W..................W" +
                        "W............D..D..W" +
                        "W..................W" +
                        "W...D..............W" +
                        "W..................W" +
                        "W...D..........D...W" +
                        "W..................W" +
                        "W...D.......D......W" +
                        "W....D.............W" +
                        "W.........D........W" +
                        "W.........D........W" +
                        "W...D..............W" +
                        "WWWWWWWWWWWWWWWWWWWW"
                , 3
        );
    }

    /**
     * For each direction in the following order, return 3 values :
     * - 1/distance to a wall
     * - 1/distance to a diamond : 0 means no diamond
     * - 1/distance to an output : 0 means no output
     * <p>
     * S - SE - E - NE - N - NW - W - SW
     * because y is aimed to the south in our coordinates system (java canvas !)
     * </p>
     */
//    @Test
    public void lookInAllDirections() {
        // Given
        GameBoardPosition playerPosition = new GameBoardPosition(1, 1);

        // When
        double[] vision = level.lookInAllDirections(playerPosition);

        // Then
        int index = 0;
        // S
        Assert.assertEquals(1.0 / 12.0, vision[index++], 0.01);
        Assert.assertEquals(0, vision[index++], 0.0);
//        Assert.assertEquals(0, vision[index++], 0.0);
        // S-E
        Assert.assertEquals(1.0 / 12.0, vision[index++], 0.01);
        Assert.assertEquals(1.0 / 3.0, vision[index++], 0.0);
//        Assert.assertEquals(0, vision[index++], 0.0);
        // E
        Assert.assertEquals(1.0 / 18.0, vision[index++], 0.01);
        Assert.assertEquals(0, vision[index++], 0.0);
//        Assert.assertEquals(0, vision[index++], 0.0);
        // N-E
        Assert.assertEquals(1.0 / 1.0, vision[index++], 0.01);
        Assert.assertEquals(0, vision[index++], 0.0);
//        Assert.assertEquals(0, vision[index++], 0.0);
        // N
        Assert.assertEquals(1.0 / 1.0, vision[index++], 0.01);
        Assert.assertEquals(0, vision[index++], 0.0);
//        Assert.assertEquals(0, vision[index++], 0.0);
        // N-W
        Assert.assertEquals(1.0 / 1.0, vision[index++], 0.01);
        Assert.assertEquals(0, vision[index++], 0.0);
//        Assert.assertEquals(0, vision[index++], 0.0);
        // W
        Assert.assertEquals(1.0 / 1.0, vision[index++], 0.01);
        Assert.assertEquals(0, vision[index++], 0.0);
//        Assert.assertEquals(0, vision[index++], 0.0);
        // S-W
        Assert.assertEquals(1.0 / 1.0, vision[index++], 0.01);
        Assert.assertEquals(0, vision[index++], 0.0);
//        Assert.assertEquals(0, vision[index++], 0.0);
    }
}