package util;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

@State(Scope.Thread)
// FIXME: alterar para os valores corretos
@Fork(value = 1)
@Warmup(iterations = 1, time = 1)
@Measurement(iterations = 1, time = 1)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
abstract public class BenchmarkConfig {

    @Param({})
    protected String currentTest;

    @Param({"-1"})
    protected int phraseSize;

    protected String[] wordsToAdd;

    protected Pair<String, Integer>[] phrases;
    protected int correctAmount;

    private Map<WordCategory, List<String>> categorizedWords;
    private Map<WordCategory, Integer> categoryCountMap;

    public abstract void addWords(String[] words);
    public abstract int countBadWords(String phrase);
    public abstract boolean checkIsBadWord(String word);

    @Setup(Level.Trial)
    public void setUp() {
        switch (currentTest) {
            case "phrases":
                addWords(FileUtils.readBadWords());
                phrases = FileUtils.readPhrases(phraseSize);
                break;
            case "words":
                addWords(FileUtils.readBadWords());
                categorizedWords = FileUtils.readWords();
                categoryCountMap = WordCategory.categoryCountMap();
                break;
            default:
                wordsToAdd = FileUtils.readGoodWords();
        }
    }

    @Benchmark
    public void insertAll(Blackhole blackhole) {
        addWords(wordsToAdd);
    }

    @Benchmark
    public void queryWords(Blackhole blackhole) {
        for (Map.Entry<WordCategory, List<String>> wordsOfCategory : categorizedWords.entrySet()) {
            WordCategory category = wordsOfCategory.getKey();

            for (String word : wordsOfCategory.getValue()) {
                if (checkIsBadWord(word)) categoryCountMap.put(category, categoryCountMap.get(category) + 1);
            }
        }
    }

    @Benchmark
    public void searchPhrases(Blackhole blackhole) {
        for (Pair<String, Integer> phrase : phrases) {
            if (countBadWords(phrase.first()) == phrase.second()) {
                correctAmount++;
            }
        }
    }

    @TearDown(Level.Trial)
    public void recordPhraseResults() {
        if (currentTest.equals("phrases")) {
            FileUtils.savePhrasesResult(getClass().getName(), correctAmount, phraseSize);
        } else if (currentTest.equals("words")) {
            for (Map.Entry<WordCategory, Integer> entry : categoryCountMap.entrySet()) {
                int total  = categorizedWords.get(entry.getKey()).size();
                int missed = entry.getValue() - total;

                FileUtils.saveWordsResult(getClass().getName(), entry.getValue(), missed, entry.getKey().name());
            }
        }
    }
}
