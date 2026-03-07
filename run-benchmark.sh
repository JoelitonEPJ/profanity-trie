if [ ! -d out/results ] || [ ! -d out/images ]; then
    mkdir -p out/{results,images}
fi

if [ ! -f data/bad_words/formatted.txt ] || [ ! -f data/good_words/formatted.txt ]; then
    python3 scripts/python/format_wordlist.py
fi

if [ ! -d data/sentences ]; then
    python3 scripts/python/sentences_generator.py
fi

declare -A classes=( [trie]="Trie" [aho]="AhoCorasick" [hash]="HashTable" [regex]="Regex" [base]="Baseline" )

if [[ -v classes[${1}] ]]; then
    run_class=${classes[${1}]}
fi

if [ -n "$2" ]; then
    run_comp=$2
elif [ -n "$1" ] && [ ! -n "$run_class" ]; then
    run_comp=$1
else
    run_all=yes
fi

mvn clean install
mvn clean package

# performance de inserção
if [[ $run_comp == insert ]] || [ -n "$run_all" ]; then
    java -jar target/benchmarks.jar ".+${run_class}Benchmark\.insertAll" -rf csv -rff out/results/insertion_perf.csv
fi

# consumo de memória 
if [[ $run_comp == memory ]] || [ -n "$run_all" ]; then
    java -jar target/benchmarks.jar ".+${run_class}Benchmark\.insertAll" -prof gc -rf csv -rff out/results/memory_usage.csv
fi

# velocidade de busca em frases e detecção
if [[ $run_comp == phrases ]] || [ -n "$run_all" ]; then
    java -jar target/benchmarks.jar ".+${run_class}Benchmark\.searchPhrases" -rf csv -rff out/results/search_phrases_perf.csv -p phraseCount=1000,5000,10000 -p record=phrases
fi

# velocidade de busca em palavras e detecção
if [[ $run_comp == words ]] || [ -n "$run_all" ]; then
    java -jar target/benchmarks.jar ".+${run_class}Benchmark\.queryWords" -rf csv -rff out/results/search_words_perf.csv -p record=words
fi

# TODO: gerar gráficos, fazer após já ter os benchmarks configurados