package hash_table;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.infra.Blackhole;

import util.BenchmarkConfig;

public class HashTableBenchmark extends BenchmarkConfig {
    @Setup(Level.Trial)
    public void setUp() {
    }

    @Override
    @Benchmark
    public void insertAll(Blackhole blackhole) {
    }

    @Override
    @Benchmark
    public void queryWords(Blackhole blackhole) {
    }

    @Override
    @Benchmark
    public void searchPhrases(Blackhole blackhole) {
    }
}
