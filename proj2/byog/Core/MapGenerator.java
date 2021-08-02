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

    private static void addBackground(TETile[][] tiles, int width, int height) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                tiles[i][j] = Tileset.NOTHING;
            }
        }
    }

    private static void generateRooms(TETile[][] tiles, Position startPos, int hallWidth, int hallHeight) {
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

    private static void generateHorHallways(TETile[][] tiles, Position startPos, int hallWidth) {
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

    private static void createHallwayBetweenRoom(Room a, Room b) {

    }

    private static void generateRandomRooms(TETile map, Room room) {

    }

    private static void generateRandomHallway(TETile map, Room room) {

    }

    public static TETile[][] generateMap(int width, int height) {
        Random RANDOM = new Random();
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

        System.out.println(roomList.size()+" rooms created.");
        return map;
    }
}
