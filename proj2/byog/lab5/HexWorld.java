package byog.lab5;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;

    private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);

    private static void addBackground(TETile[][] tiles) {

        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                tiles[i][j] = Tileset.NOTHING;
            }
        }
    }

    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(5);
        switch (tileNum) {
            case 0: return Tileset.WALL;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.GRASS;
            case 3: return Tileset.MOUNTAIN;
            case 4: return Tileset.TREE;
            default: return Tileset.NOTHING;
        }
    }

    private static void addHexagon(TETile[][] tiles, int size) {
        int startingX = (WIDTH-size) / 2;
        int startingY = 0;

        int xGap = 2 * size - 1;

        addNHexagon(tiles, size, 5, startingX, startingY);

        for (int i = 1; i <= 2; i++) {
            addNHexagon(tiles, size, 5 - i, startingX - xGap * i, startingY + size * i);
            addNHexagon(tiles, size, 5 - i, startingX + xGap * i, startingY + size * i);
        }
    }

    private static void addNHexagon(TETile[][] tiles, int size, int N, int x, int y) {
        for (int i = 0; i < N; i++) {
            addHexagon(tiles, size, x, y + i * size * 2, randomTile());
        }
    }

    private static void addHexagon(TETile[][] tiles,
                                             int size,
                                             int x,
                                             int y,
                                             TETile type
    ) {
        for (int i = 0; i < size; i++) {
            addHexagonRow(tiles, size + i * 2, x - i, y + i, type);
            addHexagonRow(tiles, size + i * 2, x - i, y + size*2 - 1 - i, type);
        }
    }

    /**
     * Add hexagon from (x, y) to (x+length-1, y)
     * @param tiles
     * @param length; describes the length of hexagon in a row
     * @param x
     * @param y
     */
    private static void addHexagonRow(TETile[][] tiles, int length, int x, int y, TETile type) {
        for(int i = 0; i < length; i++) {
            tiles[x+i][y] = type;
        }
    }

    private static boolean isLocationOk(TETile[][] tiles, int size, int x, int y) {
        int leftX = x - size + 1;
        int bottomY = y + size*2 - 1;


        if (leftX < 0 || bottomY > WIDTH) {
            return false;
        }

        return true;
    }

    public static void main(String[] args) {
        TERenderer renderer = new TERenderer();
        renderer.initialize(WIDTH, HEIGHT);

        TETile[][] tiles = new TETile[WIDTH][HEIGHT];
        addBackground(tiles);
        addHexagon(tiles, 4);

        renderer.renderFrame(tiles);
    }
}
