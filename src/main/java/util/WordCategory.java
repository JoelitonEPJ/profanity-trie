package util;

public enum WordCategory {
    GOOD_WORD, NONE, UPPER, SPACED, ENCODED, STRETCHED;

    public static WordCategory getCategory(String category) {
        return Enum.valueOf(WordCategory.class, category.toUpperCase());
    }
}