package regex;

import util.BenchmarkConfig;

public class RegexBenchmark extends BenchmarkConfig {
    
    private Regex benchmarkRegex;

    @Override
    public void addWords(String[] words) {
        benchmarkRegex = new Regex(words);
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
