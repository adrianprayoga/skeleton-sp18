/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra, Alexander Hwang
 *
 */
public class RadixSort {
    private static final int R = 256;
    /**
     * Does LSD radix sort on the passed in array with the following restrictions:
     * The array can only have ASCII Strings (sequence of 1 byte characters)
     * The sorting is stable and non-destructive
     * The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     *
     * @return String[] the sorted array
     */
    public static String[] sort(String[] asciis) {
        String[] newArray = new String[asciis.length];
        int maxNumberOfDigits = Integer.MIN_VALUE;

        for (int i = 0; i < asciis.length; i++) {
            String s = asciis[i];
            maxNumberOfDigits = maxNumberOfDigits > s.length() ? maxNumberOfDigits : s.length();
            newArray[i] = s;
        }

        for (int i = maxNumberOfDigits - 1; i >= 0 ; i--) {
            newArray = sortHelperLSD(newArray, i);
        }

        return newArray;
    }

    /**
     * LSD helper method that performs a destructive counting sort the array of
     * Strings based off characters at a specific index.
     * @param asciis Input array of Strings
     * @param index The position to sort the Strings on.
     */
    private static String[] sortHelperLSD(String[] asciis, int index) {
        int[] count = new int[R+1];
        int[] pointer = new int[R+1];

        for(String s : asciis) {
            count[getId(index, s)]++;
        }

        for (int i = 1; i < pointer.length; i++) {
            pointer[i] = pointer[i-1] + count[i-1];
        }

        String[] newArray = new String[asciis.length];

        for(int i = 0; i < asciis.length; i++) {
            String s = asciis[i];
            int id = getId(index, s);

            newArray[pointer[id]] = s;
            pointer[id]++;
        }

        return newArray;
    }

    /**
     * MSD radix sort helper function that recursively calls itself to achieve the sorted array.
     * Destructive method that changes the passed in array, asciis.
     *
     * @param asciis String[] to be sorted
     * @param start int for where to start sorting in this method (includes String at start)
     * @param end int for where to end sorting in this method (does not include String at end)
     * @param index the index of the character the method is currently sorting on
     *
     **/
    private static void sortHelperMSD(String[] asciis, int start, int end, int index) {
        // Optional MSD helper method for optional MSD radix sort
        return;
    }

    private static int getId(int index, String s) {
        if (index > s.length() - 1) {
            return 0;
        } else {
            return (int) s.charAt(index) + 1;
        }
    }
}
