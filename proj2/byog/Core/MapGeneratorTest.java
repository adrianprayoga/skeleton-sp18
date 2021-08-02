package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;

public class MapGeneratorTest {
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;

    public static void main(String[] args) {
        TETile[][] map = MapGenerator.generateMap(WIDTH, HEIGHT);

        System.out.println(TETile.toString(map));

        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(map);
    }
}
