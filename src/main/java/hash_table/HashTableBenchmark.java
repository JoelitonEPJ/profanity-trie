package hash_table;

import util.BenchmarkConfig;

public class HashTableBenchmark extends BenchmarkConfig {
    private HashTable benchmarkHashTable;

    @Override
    public void addWords(String[] words) {
        benchmarkHashTable = new HashTable(words);
    }

    @Override
    public int countBadWords(String phrase) {
        return benchmarkHashTable.countBadWords(phrase);
    }

    @Override
    public boolean checkIsBadWord(String word) {
        return benchmarkHashTable.isBadWord(word);
    }
}
