package baseline;

import util.BenchmarkConfig;

public class BaselineBenchmark extends BenchmarkConfig {

    private Baseline benchmarkBaseline;

    @Override
    public void addWords(String[] words) {
        benchmarkBaseline = new Baseline(words);
    }

    @Override
    public int countBadWords(String phrase) {
        return benchmarkBaseline.countBadWords(phrase);
    }

    @Override
    public boolean checkIsBadWord(String word) {
        return benchmarkBaseline.isBadWord(word);
    }
}
