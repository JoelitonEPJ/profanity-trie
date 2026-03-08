package util;

import java.util.HashMap;
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

    protected Map<String, Integer> phrases;
    protected int correctAmount;

    private Map<String, Category> categorizedWords;
    private Map<Category, Integer> categoryCountMap;

    public abstract void addWord(String word);
    public abstract int countBadWords(String phrase);
    public abstract boolean checkIsBadWord(String word);

    @Setup(Level.Trial)
    public void setUp() {
        switch (currentTest) {
            case "phrases":
                wordsToAdd = FileUtils.readBadWords();
                // TODO
                // phrases = FileUtils.readPhrases(phraseSize);
                break;
            case "words":
                wordsToAdd = FileUtils.readBadWords();
                // TODO
                // categorizedWords = FileUtils.readWords(phraseSize);
                categoryCountMap = Category.categoryCountMap();
                break;
            default:
                wordsToAdd = FileUtils.readGoodWords();
        }
    }

    @Benchmark
    public void insertAll(Blackhole blackhole) {
        for (String word : wordsToAdd) addWord(word);
    }

    @Benchmark
    public void queryWords(Blackhole blackhole) {
        for (Map.Entry<String, Category> word : categorizedWords.entrySet()) {
            if (checkIsBadWord(word.getKey())) {
                Category category = word.getValue();
                categoryCountMap.put(category, categoryCountMap.getOrDefault(category, 0) + 1);
            }
        }
    }

    @Benchmark
    public void searchPhrases(Blackhole blackhole) {
        for (Map.Entry<String, Integer> phrase : phrases.entrySet()) {
            if (countBadWords(phrase.getKey()) == phrase.getValue()) {
                correctAmount++;
            }
        }
    }

    @TearDown(Level.Trial)
    public void recordPhraseResults() {
        if (currentTest.equals("phrases")) {
            FileUtils.savePhrasesResult(getClass().getName(), correctAmount, phraseSize);
        } else if (currentTest.equals("words")) {
            for (Map.Entry<Category, Integer> entry : categoryCountMap.entrySet()) {
                int total = (int) categorizedWords.values().stream().filter(c -> c.equals(entry.getKey())).count();
                int missed = entry.getValue() - total;

                FileUtils.saveWordsResult(getClass().getName(), entry.getValue(), missed, entry.getKey().name());
            }
        }
    }

    private enum Category {
        NORMAL,
        HIDDEN,
        SPACED,
        ENCODED,
        STRETCHED;

        public Category getCategory(String category) {
            return Enum.valueOf(Category.class, category.toUpperCase());
        }

        public static Map<Category, Integer> categoryCountMap() {
            Map<Category, Integer> categoryCount = new HashMap<>();
            for (Category c : Category.values()) categoryCount.put(c, 0);

            return categoryCount;
        }
    }
}

