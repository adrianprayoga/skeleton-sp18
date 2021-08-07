package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MapGenerator {
    private static int MinNumberOfRooms = 30;
    private static int MaxNumberOfRooms = 100;
    private static int RoomSizeVariation = 7;
    private static int MinRoomSize = 2;
    private static Random RANDOM = new Random(2);
    private int width, height;

    public MapGenerator(int width, int height) {
        this.width = width;
        this.height = height;
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

    private void generateHorHallways(TETile[][] tiles, Position startPos, int hallWidth) {
        int hallHeight = 1;
        for(int y = 0; y < hallHeight + 2; y++) {
            for(int x = 0; x < hallWidth + 2; x++) {
                if (y == 0 || y == hallHeight + 1) {
                    tiles[startPos.x + x][startPos.y + y] = Tileset.WALL;
                } else {
                    tiles[startPos.x + x][startPos.y + y] = Tileset.FLOOR;
                }
            }
        }
    }

    private void createHallwayBetweenRoom(Room a, Room b) {

    }

    private void generateRandomRooms(TETile[][] map, Room room) {

    }

    private boolean createOneUnitHallwayV(TETile[][] map, Position position, int dir) {
        int x = position.x;
        int y = position.y;

        if(map[x][y].equals(Tileset.WALL)
                && map[x][y+2].equals(Tileset.WALL)
                && map[x][y+1].equals(Tileset.WALL)) {
            map[x][y+1] = Tileset.FLOOR;
            //TODO: Marked rooms as connected
            return true;

        } else if (map[x][y+2].equals(Tileset.WALL)
                && map[x][y+1].equals(Tileset.WALL)) {

            createOneUnitHallwayHHalf(map, new Position(x, y), "BOTTOM");
            createOneUnitHallwayHHalf(map, new Position(x+1*dir, y), "BOTTOM");
            map[x+2*dir][y] = Tileset.WALL;
            //TODO: Marked rooms as connected
            return true;
        } else if (map[x][y].equals(Tileset.WALL)
                && map[x][y+1].equals(Tileset.WALL)) {
            createOneUnitHallwayHHalf(map, new Position(x, y), "TOP");
            createOneUnitHallwayHHalf(map, new Position(x+1*dir, y), "TOP");
            map[x+2*dir][y+2] = Tileset.WALL;
            //TODO: Marked rooms as connected
            return true;
        } else {
            map[position.x][position.y] = Tileset.WALL;
            map[position.x][position.y+1] = Tileset.FLOOR;
            map[position.x][position.y+2] = Tileset.WALL;
        }
        return false;
    }

    private boolean createOneUnitHallwayH(TETile[][] map, Position position, int dir) {
        int x = position.x;
        int y = position.y;

        if(map[x][y].equals(Tileset.WALL)
                && map[x][y+2].equals(Tileset.WALL)
                && map[x][y+1].equals(Tileset.WALL)) {
            map[x][y+1] = Tileset.FLOOR;
            //TODO: Marked rooms as connected
            return true;

        } else if (map[x][y+2].equals(Tileset.WALL)
                && map[x][y+1].equals(Tileset.WALL)) {

            createOneUnitHallwayHHalf(map, new Position(x, y), "BOTTOM");
            createOneUnitHallwayHHalf(map, new Position(x+1*dir, y), "BOTTOM");
            map[x+2*dir][y] = Tileset.WALL;
            //TODO: Marked rooms as connected
            return true;
        } else if (map[x][y].equals(Tileset.WALL)
                && map[x][y+1].equals(Tileset.WALL)) {
            createOneUnitHallwayHHalf(map, new Position(x, y), "TOP");
            createOneUnitHallwayHHalf(map, new Position(x+1*dir, y), "TOP");
            map[x+2*dir][y+2] = Tileset.WALL;
            //TODO: Marked rooms as connected
            return true;
        } else {
            map[position.x][position.y] = Tileset.WALL;
            map[position.x][position.y+1] = Tileset.FLOOR;
            map[position.x][position.y+2] = Tileset.WALL;

//            if (y + 3 < width &&
//                    (map[position.x][position.y+3].equals(Tileset.WALL)
//                    || map[position.x][position.y+3].equals(Tileset.FLOOR))
//            ) {
//                if (RANDOM.nextInt(5) == 0) {
//                    map[position.x][position.y+2] = Tileset.FLOOR;
//                    map[position.x][position.y+3] = Tileset.FLOOR;
//                }
//            }
//
//            if (y - 1 > 0 &&
//                    (map[position.x][position.y-1].equals(Tileset.WALL)
//                            || map[position.x][position.y-1].equals(Tileset.FLOOR))) {
//                if (RANDOM.nextInt(5) == 0) {
//                    map[position.x][position.y] = Tileset.FLOOR;
//                    map[position.x][position.y-1] = Tileset.FLOOR;
//                }
//            }
        }
        return false;
    }

    private void createOneUnitHallwayHHalf(TETile[][] map, Position position, String type) {
        if(position.x < 0 || position.y >= width) return; // TODO: take width
        if (type == "TOP") {
            map[position.x][position.y+1] = Tileset.FLOOR;
            map[position.x][position.y+2] = Tileset.WALL;
        } else {
            map[position.x][position.y] = Tileset.WALL;
            map[position.x][position.y+1] = Tileset.FLOOR;
        }

    }


    private void generateRandomHallway(TETile[][] map,
                                              Room room,
                                              int width,
                                              int height,
                                              int direction
    ) {
        Position startingPos = direction == 1
                ? room.pickRandomRightWallPosition(RANDOM)
                : room.pickRandomLeftWallPosition(RANDOM);

        int x = startingPos.x;
        int y = startingPos.y;

        map[x][y+1] = Tileset.FLOOR;

        int i = 0;
        while(i < width) {
            i++;

            if (x + i * direction >= width) {
                map[x + i - 1][y+1] = Tileset.WALL;
                break;
            } else if (x + i * direction < 0) {
                map[0][y+1] = Tileset.WALL;
                break;
            } else {
                boolean c = createOneUnitHallwayH(map, new Position(x + i * direction, y), direction);
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
            int r = RANDOM.nextInt(5);
            if (r == 0) {
                generateRandomHallway(map, room, width, height, 1);
            } else if (r == 1) {
                generateRandomHallway(map, room, width, height, -1);
            }
        }


        System.out.println(roomList.size()+" rooms created.");
        return map;
    }
}
