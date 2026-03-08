package regex;

import util.BenchmarkConfig;

public class RegexBenchmark extends BenchmarkConfig {
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
