public class OffByOne implements CharacterComparator{
    @Override
    public boolean equalChars(char x, char y) {
        int e = Math.abs(x - y);
        return Math.abs(x - y) == 1;
    }
}
