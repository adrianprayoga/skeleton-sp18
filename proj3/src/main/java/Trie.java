import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Trie {
    private static String ACCEPTED_CHAR = "abcdefghijklmnopqrstuvwxyz ";
    public static int DIFF = 97;
    public static int CHAR_NUM = 27;
    public TrieNode head;

    class TrieNode {
        private Character c;
        boolean endOfWord = false;
        List<Long> locationIds = new ArrayList<>();
        TrieNode[] links;

        public TrieNode (Character c) {
            this.c = c;
            this.links = new TrieNode[CHAR_NUM];
        }

        public TrieNode (boolean endOfWord, Long nodeId) {
            this.endOfWord = endOfWord;
            this.links = new TrieNode[CHAR_NUM];
            if (endOfWord) {
                this.locationIds.add(nodeId);
            }
        }

        public void setEndOfWord(boolean endOfWord, Long nodeId) {
            this.endOfWord = endOfWord;
            this.locationIds.add(nodeId);
        }
    }

    public Trie () {
        this.head = new TrieNode(null);
    }

    public void addString (String word, Long nodeId) {
        char[] chars = getCleanString(word).toCharArray();

        TrieNode pointer = head;

        if (chars.length == 0) {
            pointer.locationIds.add(nodeId);
        }

        for(int i = 0; i < chars.length; i++) {
            int loc = charToIntConverter(chars[i]);
            TrieNode child = pointer.links[loc];
            if (child == null) {
                pointer.links[loc] = new TrieNode(i == chars.length - 1, nodeId);
            } else if (i == chars.length - 1) {
                child.setEndOfWord(true, nodeId);
            }

            pointer = pointer.links[loc];
        }
    }

    public boolean exist (String word) {
        char[] chars = getCleanString(word).toCharArray();
        TrieNode pointer = head;

        for(int i = 0; i < chars.length; i++) {
            int loc = charToIntConverter(chars[i]);
            TrieNode child = pointer.links[loc];
            if (child == null) {
                return false;
            }

            if (i == chars.length - 1) {
                return child.endOfWord;
            }

            pointer = pointer.links[loc];
        }

        return true;
    }

    public List<Long> getWordsWithPrefix (String prefix) {
        List<Long> locationIds = new ArrayList<>();
        char[] chars = prefix.toCharArray();

        TrieNode pointer = head;
        for(int i = 0; i < chars.length; i++) {
            int loc = charToIntConverter(chars[i]);
            TrieNode child = pointer.links[loc];
            if (child == null) {
                return locationIds;
            }

            if (i == chars.length - 1 && child.endOfWord) {
                locationIds.addAll(child.locationIds);
            }

            pointer = pointer.links[loc];
        }

        getAllWordsFromTrieNode(locationIds, pointer, prefix);
        return locationIds;
    }

    public List<Long> getWordsWithExactMatch (String word) {
        char[] chars = getCleanString(word).toCharArray();

        TrieNode pointer = head;

        if (chars.length == 0) {
            return pointer.locationIds;
        }

        for(int i = 0; i < chars.length; i++) {
            int loc = charToIntConverter(chars[i]);
            TrieNode child = pointer.links[loc];
            if (child == null) {
                return new ArrayList<>();
            }

            if (i == chars.length - 1 && child.endOfWord) {
                return child.locationIds;
            }

            pointer = pointer.links[loc];
        }

        return new ArrayList<>();
    }

    public List<Long> getAllWords () {
        List<Long> allWords = new ArrayList<>();
        getAllWordsFromTrieNode(allWords, head, "");
        return allWords;
    }

    private void getAllWordsFromTrieNode(List<Long> locationIds, TrieNode t, String prefix) {
        if (t == null) {
            return;
        }

        for (int i = 0; i < t.links.length; i++) {
            TrieNode child = t.links[i];
            if (child != null) {
                if (child.endOfWord) {
                    locationIds.addAll(child.locationIds);
                }

                getAllWordsFromTrieNode(locationIds, child, prefix + intToCharConverter(i));
            }
        }
    }

    // Assume that we only have a-z (lower case) and ( ) (space)
    private int charToIntConverter (Character character) {
        if (character.equals(' ')) {
            return 26;
        } else {
            return (int) character - DIFF;
        }
    }

    private Character intToCharConverter (int i) {
        if (i == 26) {
            return ' ';
        } else {
            return (char) (i + DIFF);
        }
    }

    private String getCleanString(String string) {
        String s = "";
        for (char c : string.toLowerCase(Locale.ROOT).toCharArray()) {
            if (ACCEPTED_CHAR.contains(String.valueOf(c))) {
                s += c;
            }
        }

        return s;
    }

//    public static void main(String[] args) {
//        Trie a = new Trie();
//        a.addString("testing", 1L);
//        a.addString("tesla", 2L);
//        a.addString("d'angelo", 3L);
//        a.addString("brazil cafe", 4L);
//        a.addString("forever 21", 5L);
//        a.addString("molly b.", 6L);
//        a.addString("9th & Harrison (Tileshop)", 7L);
//        a.addString("PetFood Express", 8L);
//        a.addString("Pet Food Express", 9L);
//        a.addString("99 cent store", 10L);
//        a.addString("Zoo", 11L);
//        a.addString("", 12L);
//
//        System.out.println(a.exist("tes"));
//        System.out.println(a.exist("t"));
//        System.out.println(a.exist("tesla"));
//        System.out.println(a.exist("s"));
//
//        System.out.println(a.getAllWords());
//        System.out.println(a.getWordsWithPrefix("s") + " []");
//        System.out.println(a.getWordsWithPrefix("tes") + " [1, 2]");
//        System.out.println(a.getWordsWithPrefix("tesl") + " [2]");
//        System.out.println(a.getWordsWithPrefix("tesla") + " [2]");
//        System.out.println(a.getWordsWithPrefix("teslaw") + " []");
//        System.out.println(a.getWordsWithPrefix("da") + " [3]");
//        System.out.println(a.getWordsWithPrefix("fo") + " [5]");
//        System.out.println(a.getWordsWithPrefix("mo") + " [6]");
//        System.out.println(a.getWordsWithPrefix("th") + " [7]");
//        System.out.println(a.getWordsWithPrefix("br") + " [4]");
//        System.out.println(a.getWordsWithPrefix("pet") + " [8, 9]");
//        System.out.println(a.getWordsWithPrefix("pet food") + " [9]");
//        System.out.println(a.getWordsWithPrefix("z") + " [11]");
//        System.out.println(a.getWordsWithExactMatch("") + " [12]");
//        System.out.println(a.getWordsWithExactMatch("pet food express") + " [9]");
//    }
}
