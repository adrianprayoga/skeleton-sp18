package byog.Core;

public class Position {
    public int x, y;
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String toString() {
        return "Position(x="+x+", y="+y+")";
    }

    public static boolean isPositionValid(Position position, int width, int height) {
        return position.x >= 0
                && position.x < width
                && position.y >= 0
                && position.y < height;
    }
}
