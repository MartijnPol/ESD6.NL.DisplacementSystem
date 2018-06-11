package util;

public final class StringHelper {

    private StringHelper() {
    }

    /**
     * Replace specific elements in a string
     *
     * @param input String that should be transformed
     * @param target Characters that should be searched for
     * @param replaceWith Characters that should be placed in the string every time the target occurs
     * @return Transformed string
     */
    public static String replace(String input, String target, String replaceWith) {
        return input.replace(target, replaceWith);
    }

    /**
     * Check if a string is empty.
     *
     * @param input String that should be checked
     * @return True when input is null or empty else false
     */
    public static boolean isEmpty(String input) {
        return input == null || input.isEmpty();
    }
}
