package byog.Core.Components;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class MapAnalyzer {
    public static boolean isVerticalUnitWall(TETile[][] map, Position position) {
        return map[position.x][position.y].equals(Tileset.WALL)
                && map[position.x][position.y+2].equals(Tileset.WALL)
                && map[position.x][position.y+1].equals(Tileset.WALL);
    }

    public static boolean isVerticalHallway(TETile[][] map, Position position) {
        return map[position.x][position.y].equals(Tileset.WALL)
                && map[position.x][position.y+2].equals(Tileset.WALL)
                && map[position.x][position.y+1].equals(Tileset.FLOOR);
    }

    public static boolean isVerticalHalfWallTop(TETile[][] map, Position position) {
        return map[position.x][position.y+2].equals(Tileset.WALL)
                && map[position.x][position.y+1].equals(Tileset.WALL);
    }

    public static boolean isVerticalHalfWallBottom(TETile[][] map, Position position) {
        return map[position.x][position.y].equals(Tileset.WALL)
                && map[position.x][position.y+1].equals(Tileset.WALL);
    }

    public static boolean isHorizontalUnitWall(TETile[][] map, Position position) {
        return map[position.x][position.y].equals(Tileset.WALL)
                && map[position.x+1][position.y].equals(Tileset.WALL)
                && map[position.x+2][position.y].equals(Tileset.WALL);
    }

    public static boolean isHorizontalHallway(TETile[][] map, Position position) {
        return map[position.x][position.y].equals(Tileset.WALL)
                && map[position.x+1][position.y].equals(Tileset.FLOOR)
                && map[position.x+2][position.y].equals(Tileset.WALL);
    }

    public static boolean isHorizontalHalfWallLeft(TETile[][] map, Position position) {
        return map[position.x][position.y].equals(Tileset.WALL)
                && map[position.x+1][position.y].equals(Tileset.WALL);
    }

    public static boolean isHorizontalHalfWallRight(TETile[][] map, Position position) {
        return map[position.x+1][position.y].equals(Tileset.WALL)
                && map[position.x+2][position.y].equals(Tileset.WALL);
    }
}
