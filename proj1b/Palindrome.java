import java.util.Objects;

public class Palindrome {
    public Deque<Character> wordToDeque(String word) {
        LinkedListDeque<Character> wordDeque = new LinkedListDeque<>();

        for (int i = 0; i < word.length(); i++) {
            wordDeque.addLast(word.charAt(i));
        }

        return wordDeque;
    }

    private boolean isPalindrome(Deque deque) {
        if(deque.isEmpty() || deque.size() == 1) {
            return true;
        }

        Character charOne = (Character) deque.removeFirst();
        Character charTwo = (Character) deque.removeLast();

        return Objects.equals(charOne, charTwo) && isPalindrome(deque);
    }

    public boolean isPalindrome(String word) {
        Deque w = wordToDeque(word);
        return isPalindrome(w);
    }

    private boolean isPalindrome(Deque deque, CharacterComparator cc) {
        if(deque.isEmpty() || deque.size() == 1) {
            return true;
        }

        Character charOne = (Character) deque.removeFirst();
        Character charTwo = (Character) deque.removeLast();

        return cc.equalChars(charOne, charTwo) && isPalindrome(deque, cc);
    }

    public boolean isPalindrome(String word, CharacterComparator cc) {
        Deque w = wordToDeque(word);
        return isPalindrome(w, cc);
    }
}
