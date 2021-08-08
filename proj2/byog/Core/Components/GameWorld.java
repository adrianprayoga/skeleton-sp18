package byog.Core.Components;

import byog.Core.RandomUtils;
import byog.SaveDemo.World;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.*;
import java.util.Random;

public class GameWorld implements Serializable {
    private int width, height;
    private TETile[][] map;
    private TETile[][] originalMap;
    private Random RANDOM;
    public Position userPosition;
    public int numberOfMoves = 0;
    public int numberOfValidMoves = 0;
    public long seed;
    public int playerHealth = 3;

    public GameWorld(int width, int height, long seed) {
        this.width = width;
        this.height = height;
        this.seed = seed;
        this.RANDOM = new Random(seed);

        MapGenerator mapGenerator = new MapGenerator(width, height, seed);
        this.map = mapGenerator.generateMap();
        addTraps();


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

    private void addTraps() {
        int numberOfTraps = 5 + RANDOM.nextInt(10);
        int i = 0;
        while (i < numberOfTraps) {
            int x = RandomUtils.uniform(RANDOM, width);
            int y = RandomUtils.uniform(RANDOM, height);

            if (map[x][y].equals(Tileset.FLOOR)) {
                map[x][y] = Tileset.TREE;
                i++;
            }
        }
    }

    public void movePlayer(String actions) {
        for (Character action : actions.toCharArray()) {
            movePlayer(action);
        };
    }

    public void movePlayer(Character action) {
        this.numberOfMoves += 1;
        switch (action) {
            case 'w':
            case 'W':
                movePlayerByXY(0, 1);
                break;
            case 'a':
            case 'A':
                movePlayerByXY(-1, 0);
                break;
            case 's':
            case 'S':
                movePlayerByXY(0, -1);
                break;
            case 'd':
            case 'D':
                movePlayerByXY(1, 0);
                break;
        }
    }

    private void movePlayerByXY(int x, int y) {
        if(isPositionValid(userPosition.x + x, userPosition.y + y)) {
            if (map[userPosition.x + x][userPosition.y + y] == Tileset.TREE) {
                this.playerHealth -= 1;
            }

            map[userPosition.x + x][userPosition.y + y] = Tileset.PLAYER;
            map[userPosition.x][userPosition.y] = originalMap[userPosition.x][userPosition.y];
            userPosition.setY(userPosition.y+y);
            userPosition.setX(userPosition.x+x);
            this.numberOfValidMoves += 1;
        }
    }

    public TETile[][] getMap() {
        return map;
    }

    private boolean isPositionValid(int x, int y) {
        return Position.isPositionValid(new Position(x, y), width, height)
                && !map[x][y].equals(Tileset.WALL);
    }

    public static void saveWorld(GameWorld w) {
        File f = new File("./world.ser");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            FileOutputStream fs = new FileOutputStream(f);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(w);
            os.close();
        }  catch (FileNotFoundException e) {
            System.out.println("file not found");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    public static GameWorld loadWorld() {
        File f = new File("./world.ser");
        if (f.exists()) {
            try {
                FileInputStream fs = new FileInputStream(f);
                ObjectInputStream os = new ObjectInputStream(fs);
                GameWorld loadWorld = (GameWorld) os.readObject();
                os.close();
                return loadWorld;
            } catch (FileNotFoundException e) {
                System.out.println("file not found");
                System.exit(0);
            } catch (IOException e) {
                System.out.println(e);
                System.exit(0);
            } catch (ClassNotFoundException e) {
                System.out.println("class not found");
                System.exit(0);
            }
        }

        return null;
    }
}
