package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;

public class MapGeneratorTest {
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;

    public static void main(String[] args) {

        Input stringInput = new Input("N1223WWWDW:Q");
        GameWorld gameWorld = new GameWorld(WIDTH, HEIGHT, stringInput.seed);
        gameWorld.movePlayer(stringInput.actionSequence);
        TETile[][] map = gameWorld.getMap();

        System.out.println(TETile.toString(map));

        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(map);
    }
}
