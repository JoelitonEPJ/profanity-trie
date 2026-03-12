package util;

import java.util.Arrays;
import java.util.HashMap;
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

    private String[] wordsToAdd;

    private Map<WordCategory, List<Pair<String, Integer>>> categorizedPhrases;
    private Map<WordCategory, Pair<Integer, Integer>> categoryCountErrorMap;


    private Map<WordCategory, List<String>> categorizedWords;
    private Map<WordCategory, Integer> categoryCountMap;

    public abstract void addWords(String[] words);

    public abstract int countBadWords(String phrase);

    public abstract boolean checkIsBadWord(String word);

    public void addWords(Pair<String, Integer>[] wordsFirstPrefix) {
        String[] words = Arrays.stream(wordsFirstPrefix).map(Pair::first).toArray(String[]::new);
        addWords(words);
    }

    public void additionalSetUp() {};

    @Setup(Level.Trial)
    public void setUp() {
        additionalSetUp();
        switch (currentTest) {
            case "phrases":
                addWords(FileUtils.readBadWords());
                categorizedPhrases = FileUtils.readPhrases(phraseSize);
                break;
            case "words":
                addWords(FileUtils.readBadWords());
                categorizedWords = FileUtils.readWords();
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
        categoryCountMap = new HashMap<>();

        for (Map.Entry<WordCategory, List<String>> wordsOfCategory : categorizedWords.entrySet()) {
            WordCategory category = wordsOfCategory.getKey();

            for (String word : wordsOfCategory.getValue()) {
                if (checkIsBadWord(word) && category != WordCategory.HIDDEN) {
                    categoryCountMap.put(category, categoryCountMap.getOrDefault(category, 0) + 1);
                }
            }
        }
    }

    @Benchmark
    public void searchPhrases(Blackhole blackhole) {
        categoryCountErrorMap = new HashMap<>();

        for (Map.Entry<WordCategory, List<Pair<String, Integer>>> phrasesOfCategory : categorizedPhrases.entrySet()) {
            WordCategory category = phrasesOfCategory.getKey();
            Pair<Integer, Integer> countError = new Pair<>(0, 0);
            categoryCountErrorMap.putIfAbsent(category, countError);

            for (Pair<String, Integer> phrase : phrasesOfCategory.getValue()) {
                int badWordsCount = countBadWords(phrase.first());

                if (badWordsCount == phrase.second()) {
                    countError.setFirst(countError.first() + 1);
                } else if (Math.abs(phrase.second() - badWordsCount) > countError.second()) {
                    countError.setSecond(Math.abs(phrase.second() - badWordsCount));
                }
            }
        }
    }

    @TearDown(Level.Trial)
    public void recordResults() {
        if (currentTest.equals("phrases")) {
            for (Map.Entry<WordCategory, Pair<Integer, Integer>> entry : categoryCountErrorMap.entrySet()) {
                int total = categorizedPhrases.get(entry.getKey()).size();
                int errorMargin = entry.getValue().second();
                int correct = entry.getValue().first();
                int missed = total - correct;

                FileUtils.savePhrasesResult(className(), correct, missed, errorMargin, entry.getKey().name(), phraseSize);
            }
        } else if (currentTest.equals("words")) {
            for (Map.Entry<WordCategory, Integer> entry : categoryCountMap.entrySet()) {
                int total = categorizedWords.get(entry.getKey()).size();
                int missed = total - entry.getValue();

                FileUtils.saveWordsResult(className(), entry.getValue(), missed, entry.getKey().name());
            }
        }
    }

    private String className() {
        return getClass().getName().replaceAll("_?jmh_?\\w+(\\.|)", "");
    }
}
