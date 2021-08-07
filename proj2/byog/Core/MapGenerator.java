package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MapGenerator {
    private static int MinNumberOfRooms = 20;
    private static int MaxNumberOfRooms = 50;
    private static int RoomSizeVariation = 10;
    private static int MinRoomSize = 2;
    private Random RANDOM;
    private int width, height;

    public MapGenerator(int width, int height, long seed) {
        this.width = width;
        this.height = height;
        this.RANDOM = new Random(seed);
    }

    private void addBackground(TETile[][] tiles, int width, int height) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                tiles[i][j] = Tileset.NOTHING;
            }
        }
    }

    private void generateRooms(TETile[][] tiles, Position startPos, int hallWidth, int hallHeight) {
        for(int y = 0; y < hallHeight + 2; y++) {
            for(int x = 0; x < hallWidth + 2; x++) {
                if (x == 0 || x == hallWidth + 1 || y == 0 || y == hallHeight + 1) {
                    tiles[startPos.x + x][startPos.y + y] = Tileset.WALL;
                } else {
                    tiles[startPos.x + x][startPos.y + y] = Tileset.FLOOR;
                }
            }
        }
    }

    private boolean createOneUnitHallwayV(TETile[][] map, Position position, int dir) {
        int x = position.x;
        int y = position.y;

        if(MapAnalyzer.isHorizontalUnitWall(map, position)) {
            if (y+dir < height && y+dir >= 0 && map[x+1][y+dir].equals(Tileset.FLOOR)) {
                map[x+1][y] = Tileset.FLOOR;
            }
            return true;
        } else if (MapAnalyzer.isHorizontalHalfWallRight(map, position)) {
            createOneUnitHallwayV(map, new Position(x, y), "LEFT");
            createOneUnitHallwayV(map, new Position(x, y+1*dir), "LEFT");
//            map[x][y+2*dir] = Tileset.WALL;
            return true;
        } else if (MapAnalyzer.isHorizontalHalfWallLeft(map, position)) {
            createOneUnitHallwayV(map, new Position(x, y), "RIGHT");
            createOneUnitHallwayV(map, new Position(x, y+1*dir), "RIGHT");
//            map[x+2][y+2*dir] = Tileset.WALL;
            return true;
        } else if (MapAnalyzer.isHorizontalHallway(map, position)) {
            return true;
        } else {
            createOneUnitHallwayV(map, new Position(x, y), "FULL");

            if (x + 4 < height && map[position.x+4][position.y].equals(Tileset.FLOOR)
            ) {
                map[position.x+2][position.y] = Tileset.FLOOR;
                map[position.x+3][position.y] = Tileset.FLOOR;
            }

            if (x - 2 > 0 && map[position.x-2][position.y].equals(Tileset.FLOOR)) {
                map[position.x][position.y] = Tileset.FLOOR;
                map[position.x-1][position.y] = Tileset.FLOOR;
            }
        }
        return false;
    }

    private boolean createOneUnitHallwayH(TETile[][] map, Position position, int dir) {
        int x = position.x;
        int y = position.y;

        if(MapAnalyzer.isVerticalUnitWall(map, position)) {
            if (x+dir < width && x+dir >= 0 && map[x+dir][y+1].equals(Tileset.FLOOR)) {
                map[x][y+1] = Tileset.FLOOR;
            }
            //TODO: Marked rooms as connected
            return true;
        } else if (MapAnalyzer.isVerticalHalfWallTop(map, position)) {
            createOneUnitHallwayH(map, new Position(x, y), "BOTTOM");
            createOneUnitHallwayH(map, new Position(x+1*dir, y), "BOTTOM");
            map[x+2*dir][y] = Tileset.WALL;
            //TODO: Marked rooms as connected
            return true;
        } else if (MapAnalyzer.isVerticalHalfWallBottom(map, position)) {
            createOneUnitHallwayH(map, new Position(x, y), "TOP");
            createOneUnitHallwayH(map, new Position(x+1*dir, y), "TOP");
            map[x+2*dir][y+2] = Tileset.WALL;
            return true;
        } else if (MapAnalyzer.isVerticalHallway(map, position)) {
            return true;
        } else {
             createOneUnitHallwayH(map, new Position(x, y), "FULL");

            if (y + 4 < height && map[position.x][position.y+4].equals(Tileset.FLOOR)
            ) {
                map[position.x][position.y+2] = Tileset.FLOOR;
                map[position.x][position.y+3] = Tileset.FLOOR;
            }

            if (y - 2 > 0 && map[position.x][position.y-2].equals(Tileset.FLOOR)) {
                map[position.x][position.y] = Tileset.FLOOR;
                map[position.x][position.y-1] = Tileset.FLOOR;
            }
        }
        return false;
    }

    private void createOneUnitHallwayH(TETile[][] map, Position position, String type) {
        if(position.x < 0 || position.y >= width) return;

        if (type == "TOP") {
            map[position.x][position.y+1] = Tileset.FLOOR;
            map[position.x][position.y+2] = Tileset.WALL;
        } else if (type == "BOTTOM") {
            map[position.x][position.y] = Tileset.WALL;
            map[position.x][position.y+1] = Tileset.FLOOR;
        } else {
            map[position.x][position.y] = Tileset.WALL;
            map[position.x][position.y+1] = Tileset.FLOOR;
            map[position.x][position.y+2] = Tileset.WALL;
        }
    }

    private void createOneUnitHallwayV(TETile[][] map, Position position, String type) {
        if(position.y < 0 || position.y >= height) return;

        if (type == "RIGHT") {
            map[position.x+1][position.y] = Tileset.FLOOR;
            map[position.x+2][position.y] = Tileset.WALL;
        } else if (type == "LEFT") {
            map[position.x][position.y] = Tileset.WALL;
            map[position.x+1][position.y] = Tileset.FLOOR;
        } else {
            map[position.x][position.y] = Tileset.WALL;
            map[position.x+1][position.y] = Tileset.FLOOR;
            map[position.x+2][position.y] = Tileset.WALL;
        }
    }


    private void generateRandomHorizontalHallway(TETile[][] map,
                                                 Room room,
                                                 int direction
    ) {
        Position startingPos = direction == 1
                ? room.pickRandomRightWallPosition(RANDOM)
                : room.pickRandomLeftWallPosition(RANDOM);

        int x = startingPos.x;
        int y = startingPos.y;

        map[x][y+1] = Tileset.FLOOR;

        int i = 0;
        int nextX = 0;
        while(i < width) {
            i++;
            nextX = x+i*direction;

            if (nextX >= width) {
                map[nextX-1][y+1] = Tileset.WALL;
                break;
            } else if (nextX < 0) {
                map[0][y+1] = Tileset.WALL;
                break;
            } else {
                boolean c = createOneUnitHallwayH(map, new Position(nextX, y), direction);
                if(c) break;
            }
        }
    }

    private void generateRandomVerticalHallway(TETile[][] map,
                                                 Room room,
                                                 int direction
    ) {
        Position startingPos = direction == 1
                ? room.pickRandomTopWallPosition(RANDOM)
                : room.pickRandomBottomWallPosition(RANDOM);

        int x = startingPos.x;
        int y = startingPos.y;

        map[x+1][y] = Tileset.FLOOR;

        int i = 0;
        int nextY = 0;
        while(i < height) {
            i++;
            nextY = y+i*direction;

            if (nextY >= height) {
                map[x+1][nextY-1] = Tileset.WALL;
                break;
            } else if (nextY < 0) {
                map[x+1][0] = Tileset.WALL;
                break;
            } else {
                boolean c = createOneUnitHallwayV(map, new Position(x, nextY), direction);
                if(c) break;
            }
        }
    }

    public TETile[][] generateMap() {
        int numOfRooms = MinNumberOfRooms + RandomUtils.uniform(RANDOM, MaxNumberOfRooms - MinNumberOfRooms);
        List<Room> roomList = new ArrayList<>();

        TETile[][] map = new TETile[width][height];
        addBackground(map, width, height);

        int i = 0;
        while (roomList.size() <= numOfRooms && i < 5000) {
            Position rootPosition = new Position(RandomUtils.uniform(RANDOM, width),
                    RandomUtils.uniform(RANDOM, height));
            int w = MinRoomSize + RandomUtils.uniform(RANDOM, RoomSizeVariation);
            int h = MinRoomSize + RandomUtils.uniform(RANDOM, RoomSizeVariation);

            Room newRoom = new Room(rootPosition, w, h);

            if(newRoom.noOverlapInRoomList(roomList) && newRoom.canBeCreated(width, height)) {
                generateRooms(map, rootPosition, w, h);
                roomList.add(newRoom);
            }
            i++;
        }

        for (Room room: roomList) {

            int r = 2;
//            if (RANDOM.nextInt(r) == 0) {
//                generateRandomHorizontalHallway(map, room, 1);
//            }
            if (RANDOM.nextInt(r) == 0 || true) {
                generateRandomHorizontalHallway(map, room, -1);
            }

            if (RANDOM.nextInt(r) == 0) {
                generateRandomVerticalHallway(map, room, 1);
            }
//            if (RANDOM.nextInt(r) == 0) {
//                generateRandomVerticalHallway(map, room, -1);
//            }
        }
        return map;
    }
}
