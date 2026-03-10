package trie;

import util.BenchmarkConfig;

public class TrieBenchmark extends BenchmarkConfig {
    ProfanityTrie trie;

    @Override
    public void addWords(String[] words) {
        trie = new ProfanityTrie(words);
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

