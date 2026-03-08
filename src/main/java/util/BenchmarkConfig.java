package util;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
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

    @Param({"none"})
    protected String recordResults;

    @Param({"-1"})
    protected int phraseSize;

    protected HashMap<String, Integer> phrases;
    protected int correctAmount;

    // Benchmarks
    public abstract void insertAll(Blackhole blackhole);
    public abstract void queryWords(Blackhole blackhole);
    public abstract void searchPhrases(Blackhole blackhole);

    @TearDown(Level.Trial)
    public void recordPhraseResults() {
        if (recordResults.equals("phrases")) {
            FileUtils.savePhrasesResult(getClass().getName(), correctAmount, phraseSize);
        } else if (recordResults.equals("words")) {
            // TODO
        }
    }
}

