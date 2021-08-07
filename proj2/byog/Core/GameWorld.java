package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

public class GameWorld {
    private int width, height;
    private TETile[][] map;
    private TETile[][] originalMap;
    private Random RANDOM;
    private Position userPosition;

    public GameWorld(int width, int height, long seed) {
        this.width = width;
        this.height = height;
        this.RANDOM = new Random(seed);

        MapGenerator mapGenerator = new MapGenerator(width, height, seed);
        this.map = mapGenerator.generateMap();

        this.originalMap = new TETile[width][height];
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                this.originalMap[i][j] = this.map[i][j];
            }
        }

        addPlayer();
    }

    private void addPlayer() {
        while (true) {
            int x = RandomUtils.uniform(RANDOM, width);
            int y = RandomUtils.uniform(RANDOM, height);

            if (map[x][y].equals(Tileset.FLOOR)) {
                map[x][y] = Tileset.PLAYER;
                this.userPosition = new Position(x, y);
                break;
            }
        }
    }

    public void movePlayer(String actions) {
        for (Character action : actions.toCharArray()) {
            movePlayer(action);
        };
    }

    private void movePlayer(Character action) {
        switch (action) {
            case 'W':
                movePlayerByXY(0, 1);
                break;
            case 'A':
                movePlayerByXY(-1, 0);
                break;
            case 'S':
                movePlayerByXY(0, -1);
                break;
            case 'D':
                movePlayerByXY(1, 0);
                break;
        }
    }

    private void movePlayerByXY(int x, int y) {
        if(isPositionValid(userPosition.x + x, userPosition.y + y)) {
            map[userPosition.x + x][userPosition.y + y] = Tileset.PLAYER;
            map[userPosition.x][userPosition.y] = originalMap[userPosition.x][userPosition.y];
            userPosition.setY(userPosition.y+y);
            userPosition.setX(userPosition.x+x);
        }
    }

    public TETile[][] getMap() {
        return map;
    }

    private boolean isPositionValid(int x, int y) {
        return Position.isPositionValid(new Position(x, y), width, height)
                && !map[x][y].equals(Tileset.WALL);
    }
}
