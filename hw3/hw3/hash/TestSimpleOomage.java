package hw3.hash;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;


import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;


public class TestSimpleOomage {

    @Test
    public void testHashCodeDeterministic() {
        SimpleOomage so = SimpleOomage.randomSimpleOomage();
        int hashCode = so.hashCode();
        for (int i = 0; i < 100; i += 1) {
            assertEquals(hashCode, so.hashCode());
        }
    }

    @Test
    public void testHashCodePerfect() {
        SimpleOomage oo1 = new SimpleOomage(5, 10, 20);
        SimpleOomage oo2 = new SimpleOomage(5, 10, 20);
        SimpleOomage oo3 = new SimpleOomage(10, 20, 5);
        SimpleOomage oo4 = new SimpleOomage(5, 15, 15);

        SimpleOomage oo6 = new SimpleOomage(0, 5, 0);
        SimpleOomage oo7 = new SimpleOomage(0, 0, 155);
        SimpleOomage oo8 = new SimpleOomage(5, 0, 0);
        SimpleOomage oo9 = new SimpleOomage(0, 155, 0);


        assertEquals(oo1.hashCode(), oo2.hashCode());
        assertNotEquals(oo1.hashCode(), oo3.hashCode());
        assertNotEquals(oo1.hashCode(), oo4.hashCode());
        assertNotEquals(oo6.hashCode(), oo7.hashCode());
        assertNotEquals(oo8.hashCode(), oo9.hashCode());

//        List<SimpleOomage> ls = new ArrayList<>();
//        for (int x = 0; x <= 255; x += 5) {
//            for (int y = 0; y <= 255; y += 5) {
//                for (int z = 0; z <= 255; z += 5) {
//                    ls.add(new SimpleOomage(x, y, z));
//                }
//            }
//        }
//
//        for (int i = 0; i < ls.size(); i++) {
//            for (int j = i+1; j < ls.size(); j++) {
//                if (ls.get(i).equals(ls.get(j))) {
//                    assertEquals(ls.get(i), ls.get(j));
//                } else {
//                    assertNotEquals(ls.get(i), ls.get(j));
//                }
//            }
//        }

    }

    @Test
    public void testEquals() {
        SimpleOomage ooA = new SimpleOomage(5, 10, 20);
        SimpleOomage ooA2 = new SimpleOomage(5, 10, 20);
        SimpleOomage ooB = new SimpleOomage(50, 50, 50);
        assertEquals(ooA, ooA2);
        assertNotEquals(ooA, ooB);
        assertNotEquals(ooA2, ooB);
        assertNotEquals(ooA, "ketchup");
    }


    @Test
    public void testHashCodeAndEqualsConsistency() {
        SimpleOomage ooA = new SimpleOomage(5, 10, 20);
        SimpleOomage ooA2 = new SimpleOomage(5, 10, 20);
        HashSet<SimpleOomage> hashSet = new HashSet<>();
        hashSet.add(ooA);
        assertTrue(hashSet.contains(ooA2));
    }


    @Test
    public void testRandomOomagesHashCodeSpread() {
        List<Oomage> oomages = new ArrayList<>();
        int N = 10000;

        for (int i = 0; i < N; i += 1) {
            oomages.add(SimpleOomage.randomSimpleOomage());
        }

        assertTrue(OomageTestUtility.haveNiceHashCodeSpread(oomages, 10));
    }

    /** Calls tests for SimpleOomage. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestSimpleOomage.class);
    }
}
