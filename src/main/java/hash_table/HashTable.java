package hash_table;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class HashTable {

    private final Set<String> badWords;

    public HashTable(String[] words) {
        this.badWords = new HashSet<>();
        badWords.addAll(Arrays.asList(words));
    }

    public int countBadWords(String phrase) {
        String[] words = phrase.split(" ");
        int count = 0;

        for (String word : words) {
            if (badWords.contains(word)) {
                count++;
            }
        }

        return count;
    }

    public boolean isBadWord(String word) {
        return badWords.contains(word);
    }
}
