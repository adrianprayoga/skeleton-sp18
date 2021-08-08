package byog.Core;

import byog.TileEngine.TETile;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameTest {

    @Test
    public void playWithInputStringTest() {
        Game game1 = new Game();
        TETile[][] tiles1 = game1.playWithInputString("N999SDDDWWWDDD");

        Game game2 = new Game();
        game2.playWithInputString("N999SDDD:Q");
        TETile[][] tiles2 = game2.playWithInputString("LWWWDDD");

        assertArrayEquals(tiles1, tiles2);

        Game game3 = new Game();
        game3.playWithInputString("N999SDDD:Q");
        game3.playWithInputString("LWWW:Q");
        TETile[][] tiles3 = game3.playWithInputString("LDDD:Q");

        assertArrayEquals(tiles1, tiles3);
    }



}
