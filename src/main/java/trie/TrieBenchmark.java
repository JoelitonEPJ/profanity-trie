package trie;

import util.BenchmarkConfig;

public class TrieBenchmark extends BenchmarkConfig {
    @Override
    public void addWords(String[] words) {
    }

    @Override
    public int countBadWords(String phrase) {
        return 0;
    }

    @Override
    public boolean checkIsBadWord(String word) {
        return false;
    }
}

