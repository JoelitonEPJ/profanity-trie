package aho_corasick;

import util.BenchmarkConfig;

public class AhoCorasickBenchmark extends BenchmarkConfig {
    
    @Override
    public void additionalSetUp() {}
    
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
