package byog.Core;

import byog.Core.Components.GameWorld;
import byog.Core.Components.Position;
import byog.SaveDemo.World;
import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.*;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        StdDraw.enableDoubleBuffering();
        drawMainPage();


        GameWorld gameWorld = mainPageOperations();
        if (gameWorld == null) {
            String seed = seedPageOperations();
            gameOperations(seed, null);
        } else {
            gameOperations(null, gameWorld);
        }

    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().
        
        String upperCaseInput = input.toUpperCase();
        Input stringInput = new Input(upperCaseInput);
        GameWorld gameWorld;

        if (upperCaseInput.startsWith("N")) {
            gameWorld = new GameWorld(WIDTH, HEIGHT, stringInput.seed);

        } else if (upperCaseInput.startsWith("L")) {
            System.out.println("loading existing game");
            gameWorld = GameWorld.loadWorld();
            if (gameWorld == null) {
                return null;
            }
        } else {
            return null;
        }

        gameWorld.movePlayer(stringInput.actionSequence);
        if (stringInput.quitAndSave) {
            GameWorld.saveWorld(gameWorld);
        }
        return gameWorld.getMap();
    }

    private static void drawMainPage() {
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(0.5, 0.8, "TILE-WORLD: THE GAME");
        StdDraw.text(0.5, 0.6, "NEW GAME (N)");
        StdDraw.text(0.5, 0.55, "LOAD GAME (L)");
        StdDraw.text(0.5, 0.5, "QUIT (Q)");
        StdDraw.show();
    }

    private static void showSeed(String seed) {
        StdDraw.text(0.5, 0.35, "ENTER SEED");
        StdDraw.text(0.5, 0.3, seed.equals("") ? "?" : seed);
        StdDraw.text(0.5, 0.25, "PRESS (S) TO START");
        StdDraw.text(0.5, 0.20, "PRESS (R) TO REMOVE THE LAST DIGIT");
        StdDraw.show();
    }

    private static void showLoadError() {
        StdDraw.text(0.5, 0.35, "NO SAVE DATA FOUND! PLEASE START A NEW GAME.");
        StdDraw.show();
    }

    private GameWorld mainPageOperations() {
        outer: while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();

                switch (c) {
                    case 'N':
                    case 'n':
                        drawMainPage();
                        showSeed("");
                        break outer;
                    case 'L':
                    case 'l':
                        GameWorld w = GameWorld.loadWorld();
                        if (w == null) {
                            showLoadError();
                        } else {
                            return w;
                        }
                        break;
                    case 'Q':
                    case 'q':
                        System.exit(0);
                        break outer;
                    default:
                }
            }
        }

        return null;
    }

    private String seedPageOperations() {
        String seed = "";
        outer: while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();

                if (c == 'Q' || c == 'q') {
                    System.exit(0);
                } else if (c == 'S' || c == 's') {
                    break outer;
                }  else if (c == 'R' || c == 'r') {
                    if (seed.length() > 0) {
                        seed = seed.substring(0, seed.length()-1);
                        drawMainPage();
                        showSeed(seed);
                    }
                } else if (Character.isDigit(c)) {
                    seed += c;
                    drawMainPage();
                    showSeed(seed);
                }
            }
        }

        return seed;
    }

    private String gameOperations(String seed, GameWorld loadedData) {

        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH + 4, HEIGHT + 8, 2, 4);

        GameWorld gameWorld = loadedData == null
                ? new GameWorld(WIDTH, HEIGHT, Long.valueOf(seed))
                : loadedData;

        drawMap(ter, gameWorld);

        boolean exitInitiated = false;

        outer: while (true) {
            drawMap(ter, gameWorld);

            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();

                if (c == ':') {
                    exitInitiated = true;
                    continue;
                } else if (exitInitiated && (c == 'Q' || c == 'q')) {
                    GameWorld.saveWorld(gameWorld);
                    System.exit(0);
                }

                exitInitiated = false;
                gameWorld.movePlayer(c);
            }
        }
    }

    private void drawMap(TERenderer ter, GameWorld gameWorld) {
        StdDraw.enableDoubleBuffering();

        ter.addFrame(gameWorld.getMap());

        StdDraw.setPenColor(StdDraw.WHITE);
        Font smallFont = new Font("Monaco", Font.BOLD, 12);
        StdDraw.setFont(smallFont);
        StdDraw.textLeft(1, HEIGHT + 7, "Number of Moves: " + gameWorld.numberOfValidMoves);
        StdDraw.text(WIDTH/2 + 1, HEIGHT + 7, "Game of Tiles");
        StdDraw.textRight(WIDTH + 3, HEIGHT + 7, "Game Seed: " + gameWorld.seed);

        StdDraw.textLeft(1, 1, "Life: ");
        for (int i = 0; i < gameWorld.playerHealth; i++) {
            StdDraw.filledCircle(4 + i, 1, 0.3);
        }

        StdDraw.line(0, HEIGHT + 6, WIDTH + 4, HEIGHT + 6);
        StdDraw.textRight(WIDTH + 3, 1, "Press G for Guide");
        drawLabel(gameWorld.getMap(), new Position((int) StdDraw.mouseX(), (int) StdDraw.mouseY()));
        StdDraw.line(0, 2, WIDTH + 4, 2);

        StdDraw.show();
    }

    private void drawLabel(TETile[][] map, Position pos) {
        StdDraw.textLeft(WIDTH/2 + 1, 1, getFloorLabel(map, pos));
    }

    private String getFloorLabel(TETile[][] map, Position pos) {
        if (Position.isPositionValid(new Position(pos.x - 2, pos.y - 4), WIDTH, HEIGHT)) {
            TETile tile = map[pos.x - 2][pos.y - 4];

            if (tile.equals(Tileset.WALL)) {
                return "Wall";
            } else if (tile.equals(Tileset.FLOOR)) {
                return "Floor";
            } else if (tile.equals(Tileset.PLAYER)) {
                return "Player";
            } else if (tile.equals(Tileset.NOTHING)) {
                return "Nothing";
            }
        }

        return "";
    }

}
