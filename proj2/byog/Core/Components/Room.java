package byog.Core.Components;

import byog.Core.RandomUtils;

import java.util.List;
import java.util.Random;

public class Room {
    public Position corner1; // bottom left
    public Position corner2; // bottom right
    public Position corner3; // top right
    public Position corner4; // top left

    public int width;
    public int height;
    public boolean isConnected = false;
    private int SPACE_BETWEEN_ROOMS = 1;

    public Room(Position position, int width, int height) {
        this.corner1 = position;
        this.corner2 = new Position(position.x + width + 1, position.y);
        this.corner3 = new Position(position.x + width + 1, position.y + height + 1);
        this.corner4 = new Position(position.x, position.y + height + 1);

        this.width = width;
        this.height = height;

    }

    public boolean isInRoom(Position position) {
        // +/-1 is to ensure that each room are spaced more than 1 unit apart
        return position.x >= this.corner1.x - SPACE_BETWEEN_ROOMS
                && position.x <= this.corner3.x + SPACE_BETWEEN_ROOMS
                && position.y >= this.corner1.y - SPACE_BETWEEN_ROOMS
                && position.y <= this.corner3.y + SPACE_BETWEEN_ROOMS;
    }

    public boolean overlap(Room otherRoom) {
        return this.isInRoom(otherRoom.corner1)
                || this.isInRoom(otherRoom.corner2)
                || this.isInRoom(otherRoom.corner3)
                || this.isInRoom(otherRoom.corner4);
    }

    public boolean canBeCreated (int width, int height) {
        return corner3.x < width && corner3.y < height;
    }

    public boolean noOverlapInRoomList(List<Room> roomList) {
        return roomList.stream().noneMatch(r -> this.overlap(r) || r.overlap(this));
    }

    public Position pickRandomRightWallPosition (Random RANDOM) {
        int x = corner3.x + RandomUtils.uniform(RANDOM, 1);
        int y = corner1.y + RandomUtils.uniform(RANDOM, height);
        return new Position(corner3.x, y);
    }
    public Position pickRandomLeftWallPosition (Random RANDOM) {
        int y = corner1.y + RandomUtils.uniform(RANDOM, height);
        return new Position(corner1.x, y);
    }

    public Position pickRandomTopWallPosition (Random RANDOM) {
        int x = corner1.x + RandomUtils.uniform(RANDOM, width);
        return new Position(x, corner3.y);
    }
    public Position pickRandomBottomWallPosition (Random RANDOM) {
        int x = corner1.x + RandomUtils.uniform(RANDOM, width);
        return new Position(x, corner1.y);
    }

    public String toString() {
        return "corners "
                + corner1.toString() + " "
                + corner2.toString() + " "
                + corner3.toString() + " "
                + corner4.toString() + " "
                + "w="+width+" h="+height;
    }
}
