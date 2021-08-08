package byog.Core.Components;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RoomTest {

    @Test
    public void isInRoomTest() {
        Room r = new Room(new Position(0, 0), 4, 3);

        /*
        * # # # # # # (5, 4)
        * # . . . . #
        * # . . . . #
        * # . . . . #
        * # # # # # # (5, 0)
        * */

        assertTrue(r.isInRoom(new Position(4, 2)));
        assertTrue(r.isInRoom(new Position(1, 1)));
        assertFalse(r.isInRoom(new Position(4, 8)));

        Room r2 = new Room(new Position(22, 6), 4, 4);

        assertTrue(r2.isInRoom(new Position(23, 7)));
        assertTrue(r2.isInRoom(new Position(23, 10)));
    }

    @Test
    public void noOverlapTest() {
        List<Room> roomList = new ArrayList<>();
        roomList.add(new Room(new Position(22, 6), 4, 4));
        Room testRoom = new Room(new Position(20, 7), 2, 2);

        assertFalse(testRoom.noOverlapInRoomList(roomList));
    }
}
