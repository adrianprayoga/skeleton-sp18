import static org.junit.Assert.*;
import org.junit.Test;

public class TestArrayDequeGold {
    @Test
    public void testArrayDequeGold () {
        StudentArrayDeque<Integer> sad1 = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> sol = new ArrayDequeSolution<>();

        String errorMessage = "";

        Integer numberOfRep = 30 + StdRandom.uniform(20);

        for (int i = 0; i < numberOfRep; i += 1) {
            Integer randNumber = StdRandom.uniform(100);
            Integer randNumber2 = StdRandom.uniform(100);

            errorMessage += "addFirst(" + randNumber +")\n";
            sad1.addFirst(randNumber);
            sol.addFirst(randNumber);

            assertEquals(errorMessage, sol.get(0), sad1.get(0));

            errorMessage += "addLast(" + randNumber2 +")\n";
            sad1.addLast(randNumber2);
            sol.addLast(randNumber2);

            assertEquals(sol.get(sol.size() - 1), sad1.get(sad1.size() - 1));
        }

        errorMessage += "size()\n";
        assertEquals(errorMessage, sol.size(), sad1.size());

        for (int i = 0; i < numberOfRep; i += 1) {

            errorMessage += "removeFirst()\n";
            Integer studentF = sad1.removeFirst();
            Integer solutionF = sol.removeFirst();

            assertEquals(errorMessage, solutionF, studentF);
            assertEquals(errorMessage, sol.get(0), sad1.get(0));
        }

        for (int i = 0; i < numberOfRep; i += 1) {

            errorMessage += "removeLast()\n";
            Integer studentL = sad1.removeLast();
            Integer solutionL = sol.removeLast();

            assertEquals(errorMessage, solutionL, studentL);
            assertEquals(errorMessage, sol.get(sol.size() - 1), sad1.get(sad1.size() - 1));
        }


    }
}
