import java.util.ArrayList;
import java.util.List;

public class Trie {

    private static String ACCEPTED_CHAR = "abcdefghijklmnopqrstuvwxyz_";
    public static int DIFF = 96;
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
        char[] chars = word.toCharArray();

        TrieNode pointer = head;
        for(int i = 0; i < chars.length; i++) {
            int loc = charToIntConverter(chars[i]);
            if (loc >= 0) {
                TrieNode child = pointer.links[loc];
                if (child == null) {
                    pointer.links[loc] = new TrieNode(i == chars.length - 1, nodeId);
                } else if (i == chars.length - 1) {
                    child.setEndOfWord(true, nodeId);
                }

                pointer = pointer.links[loc];
            }
        }
    }

    public boolean exist (String word) {
        char[] chars = word.toCharArray();
        TrieNode pointer = head;
        for(int i = 0; i < chars.length; i++) {
            int loc = charToIntConverter(chars[i]);
            if (loc >= 0) {
                TrieNode child = pointer.links[loc];
                if (child == null) {
                    return false;
                }

                if (i == chars.length - 1) {
                    return child.endOfWord;
                }

                pointer = pointer.links[loc];
            }
        }

        return true;
    }

    public List<Long> getWordsWithPrefix (String prefix) {
        List<Long> locationIds = new ArrayList<>();
        char[] chars = prefix.toCharArray();

        TrieNode pointer = head;
        for(int i = 0; i < chars.length; i++) {
            int loc = charToIntConverter(chars[i]);
            if (loc >= 0) {
                TrieNode child = pointer.links[loc];
                if (child == null) {
                    return locationIds;
                }

                if (i == chars.length - 1 && child.endOfWord) {
                    locationIds.addAll(child.locationIds);
                }

                pointer = pointer.links[loc];
            }
        }

        getAllWordsFromTrieNode(locationIds, pointer, prefix);
        return locationIds;
    }

    public List<Long> getWordsWithExactMatch (String word) {
        char[] chars = word.toCharArray();

        TrieNode pointer = head;
        for(int i = 0; i < chars.length; i++) {
            int loc = charToIntConverter(chars[i]);
            if (loc >= 0) {
                TrieNode child = pointer.links[loc];
                if (child == null) {
                    return new ArrayList<>();
                }

                if (i == chars.length - 1 && child.endOfWord) {
                    return child.locationIds;
                }

                pointer = pointer.links[loc];
            }
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

    // Assume that we only have a-z (lower case) and _ (underscore)
    private int charToIntConverter (Character character) {
        if (!ACCEPTED_CHAR.contains(String.valueOf(character))) {
            return -1;
        } else if (character.equals("_")) {
            return 0;
        } else {
            return (int) character - DIFF;
        }
    }

    private Character intToCharConverter (int i) {
        if (i == 0) {
            return '_';
        } else {
            return (char) (i + DIFF);
        }
    }

//    public static void main(String[] args) {
//        Trie a = new Trie();
//        a.addString("testing", 1L);
//        a.addString("tesla", 2L);
//        a.addString("d'angelo", 3L);
//
//        System.out.println(a.exist("tes"));
//        System.out.println(a.exist("t"));
//        System.out.println(a.exist("tesla"));
//        System.out.println(a.exist("s"));
//
//        System.out.println(a.getAllWords());
//        System.out.println(a.getWordsWithPrefix("s"));
//        System.out.println(a.getWordsWithPrefix("tes"));
//        System.out.println(a.getWordsWithPrefix("tesl"));
//        System.out.println(a.getWordsWithPrefix("tesla"));
//        System.out.println(a.getWordsWithPrefix("teslaw"));
//        System.out.println(a.getWordsWithPrefix("da"));
//    }
}
