package regex;

import java.util.Map;

import util.BenchmarkConfig;
import util.FileUtils;

public class RegexBenchmark extends BenchmarkConfig {
    
    private Regex benchmarkRegex;
    private Map<Character, String[]> leetMap;

    @Override
    public void setUp() {
        super.setUp();
        leetMap = FileUtils.readCsvCharToLeetMap();
    }

    @Override
    public void addWords(String[] words) {
        benchmarkRegex = new Regex(words, leetMap);
    }

    @Override
    public int countBadWords(String phrase) {
        return benchmarkRegex.countMatches(phrase);
    }

    @Override
    public boolean checkIsBadWord(String word) {
        return benchmarkRegex.matches(word);
    }
}
