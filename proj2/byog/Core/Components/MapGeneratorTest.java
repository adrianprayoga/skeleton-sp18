package byog.Core.Components;

import byog.Core.Components.GameWorld;
import byog.Core.Input;
import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;

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
        ter.initialize(WIDTH+4, HEIGHT+8, 2, 4);
        ter.renderFrame(map);

        StdDraw.setPenColor(StdDraw.WHITE);
        Font smallFont = new Font("Monaco", Font.BOLD, 12);
        StdDraw.setFont(smallFont);
        StdDraw.textLeft(1, HEIGHT + 7, "TESTING");
        StdDraw.line(0, HEIGHT+6, WIDTH+4, HEIGHT+6);
        StdDraw.line(0, 2, WIDTH+4, 2);

        StdDraw.show();
    }
}
