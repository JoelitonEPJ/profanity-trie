package util;

public enum WordCategory {
    NONE, UPPER, HIDDEN, SPACED, ENCODED, STRETCHED;

    public static WordCategory getCategory(String category) {
        return Enum.valueOf(WordCategory.class, category.toUpperCase());
    }
}