package util;

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
@Fork(value = 1)
@Warmup(iterations = 1, time = 1)
@Measurement(iterations = 1, time = 1)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
abstract public class BenchmarkConfig {

    @Param({"none"})
    private String recordResults;

    @Param({"-1"})
    protected int phraseSize;

    // private int correctAmount;

    public abstract void insertAll(Blackhole blackhole);
    public abstract void queryWords(Blackhole blackhole);
    public abstract void searchPhrases(Blackhole blackhole);

    @TearDown(Level.Trial)
    public void recordPhraseResults() {
        if (recordResults.equals("phrases")) {
            // salva os resultados das frases (erros/acertos)
        } else if (recordResults.equals("words")) {
            // salva os resultados das palavras (erros/acertos por categoria)
        }
    }
}

