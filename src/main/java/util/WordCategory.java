package util;

import java.util.HashMap;
import java.util.Map;

public enum WordCategory {
    NONE, UPPER, HIDDEN, SPACED, ENCODED, STRETCHED;

    public static WordCategory getCategory(String category) {
        return Enum.valueOf(WordCategory.class, category.toUpperCase());
    }

    public static Map<WordCategory, Integer> categoryCountMap() {
        Map<WordCategory, Integer> categoryCount = new HashMap<>();
        for (WordCategory c : WordCategory.values()) {
            categoryCount.put(c, 0);
        }

        return categoryCount;
    }
}