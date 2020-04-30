package net.experiment.ai.simplegame.game;

import javafx.scene.image.Image;
import net.experiment.ai.simplegame.Main;

public class GameImage {

    public static final GameImage WALL = new GameImage("/wall-bricks.jpg");

    private Image image;

    public GameImage(String resource) {
        image = new Image(getResource(resource), 20, 20, true, true);
    }

    private static String getResource(String filename) {
        return Main.class.getResource(filename).toString();
    }

    public Image getImage() {
        return image;
    }
}
