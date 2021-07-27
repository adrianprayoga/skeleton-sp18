import static org.junit.Assert.*;
import org.junit.Test;

public class FlikTest {

    @Test
    public void isSameNumberTest() {
        assertFalse(Flik.isSameNumber(1, 2));
        assertTrue(Flik.isSameNumber(1, 1));
        assertTrue(Flik.isSameNumber(0, 0));
        assertTrue(Flik.isSameNumber(128, 128));
    }

}
