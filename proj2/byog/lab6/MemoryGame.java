package byog.lab6;

import byog.Core.RandomUtils;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class MemoryGame {
    private int width;
    private int height;
    private int round;
    private Random rand;
    private boolean gameOver;
    private boolean playerTurn;
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {
//        if (args.length < 1) {
//            System.out.println("Please enter a seed");
//            return;
//        }

        int seed = 1; //Integer.parseInt(args[0]);

        MemoryGame game = new MemoryGame(40, 40);
        game.startGame(seed);
    }

    public MemoryGame(int width, int height) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
    }

    public String generateRandomString(int n, Random RANDOM) {
        String randomString = "";
        for(int i = 0; i < n; i++) {
            randomString += CHARACTERS[RANDOM.nextInt(CHARACTERS.length)];
        }

        return randomString;
    }

    public void drawFrame(String s) {
        StdDraw.clear();
        StdDraw.line(0, 38, 40, 38);
        Font font = new Font("Monaco", Font.BOLD, 15);
        StdDraw.setFont(font);
        StdDraw.text(5, 39,"Round: ");
        StdDraw.text(20, 39,"Watch!");
        StdDraw.text(20, 20, s);
        StdDraw.show();
    }

    public void drawUserFrame(String s) {
        StdDraw.clear();
        StdDraw.line(0, 38, 40, 38);
        Font font = new Font("Monaco", Font.BOLD, 15);
        StdDraw.setFont(font);
        StdDraw.text(5, 39,"Round: ");
        StdDraw.text(20, 39,"Type!");
        StdDraw.text(20, 22, "TYPE YOUR ANSWER!");
        StdDraw.text(20, 18, s);
        StdDraw.show();
    }

    public void flashSequence(String letters) {
        for (Character c : letters.toCharArray()) {
            drawFrame("");
            StdDraw.pause(500);
            drawFrame(c.toString());
            StdDraw.pause(1000);
        }
    }

    public String solicitNCharsInput(int n) {
        drawUserFrame("");

        String s = "";
        outer: while(s.length() < n) {
            if(StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();

                if (c == ':') {
                    break outer;
                }

                s += String.valueOf(c);
                drawUserFrame(s);
            }
        }

        return s;
    }

    public void startGame(int seed) {
        Random RANDOM = new Random(seed);

        int numOfChar = 1;
        while (numOfChar < 10) {
            String randomString = generateRandomString(numOfChar, RANDOM);

            flashSequence(randomString);
            String userInput = solicitNCharsInput(numOfChar);

            System.out.println("output "+ userInput);

            if(!userInput.equals(randomString)) {
                StdDraw.clear();
                StdDraw.text(20, 24, "WRONG INPUT! CORRECT ANSWER IS");
                StdDraw.text(20, 20, randomString);
                StdDraw.text(20, 16, "YOU MADE IT TO ROUND " + numOfChar);
                StdDraw.show();
                break;
            }

            numOfChar++;
        }

    }

}
