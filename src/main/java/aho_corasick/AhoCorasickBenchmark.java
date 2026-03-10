package aho_corasick;

import java.util.ArrayList;
import java.util.Map;

import util.BenchmarkConfig;
import util.FileUtils;

public class AhoCorasickBenchmark extends BenchmarkConfig {

    private AhoCorasick benchmarkAhoCorasick;
    private Map<Character, ArrayList<Character>> leetMap;
    
    @Override
    public void setUp() {
        super.setUp();
        leetMap = FileUtils.readCsvLeetToCharMap();
    }

    @Override
    public void addWords(String[] words) {
        benchmarkAhoCorasick = new AhoCorasick(words, leetMap);
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
