package trie;

import java.util.ArrayList;
import java.util.Map;

import util.BenchmarkConfig;
import util.FileUtils;
import util.Pair;

public class TrieBenchmark extends BenchmarkConfig {

    private Trie benchmarkTrie;
    private Map<Character, ArrayList<Character>> leetMap;

    @Override
    public void additionalSetUp() {
        leetMap = FileUtils.readCsvLeetToCharMap();
    }

    @Override
    public void addWords(String[] words) {
        benchmarkTrie = new Trie(words, leetMap);
    }

    @Override
    public void addWords(Pair<String, Integer>[] wordsFirstPrefix) {
        benchmarkTrie = new Trie(wordsFirstPrefix, leetMap);
    }

    @Override
    public int countBadWords(String phrase) {
        return benchmarkTrie.countBadWords(phrase);
    }

    @Override
    public boolean checkIsBadWord(String word) {
        return benchmarkTrie.checkIsBadWord(word);
    }
}

