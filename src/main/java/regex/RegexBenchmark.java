package regex;

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
@Fork(value = 1)
@Warmup(iterations = 1, time = 1)
@Measurement(iterations = 1, time = 1)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)

public class RegexBenchmark {
    @Param({"1000", "5000", "10000"})
    private int phraseSize;

    private int correctAmount;

    @Setup(Level.Trial)
    public void setUp() {
    }

    @Benchmark
    public void insertAll(Blackhole blackhole) {
    }

    @Benchmark
    public void searchPhrases(Blackhole blackhole) {
    }

    @TearDown(Level.Trial)
    public void recordPhraseResults() {
    }
}

