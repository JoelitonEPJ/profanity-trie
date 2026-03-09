package aho_corasick;

import util.BenchmarkConfig;

public class AhoCorasickBenchmark extends BenchmarkConfig {

    private AhoCorasick benchmarkAhoCorasick;

    @Override
    public void addWords(String[] words) {
        benchmarkAhoCorasick = new AhoCorasick(words);
    }

    @Override
    public int countBadWords(String phrase) {
        return benchmarkAhoCorasick.countBadWords(phrase);
    }

    @Override
    public boolean checkIsBadWord(String word) {
        return benchmarkAhoCorasick.isBadWord(word);
    }
}
