package trie;

import util.BenchmarkConfig;

public class TrieBenchmark extends BenchmarkConfig {
    Trie trie;

    @Override
    public void addWords(String[] words) {
        trie = new Trie(words);
    }

    @Override
    public int countBadWords(String phrase) {
        return trie.countBadWords(phrase);
    }

    @Override
    public boolean checkIsBadWord(String word) {
        return trie.checkIsBadWord(word);
    }
}

