# /usr/bin/env bash

set -e

help() {
    echo "usage: run-benchmark.sh [class] [benchmark]        "
    echo "                        [benchmark]                "
    echo "                        [-h --help]                "
    echo
    echo "       class       one of  ${!classes[@]}          "
    echo "       benchmark   one of  ${benchmarks[*]}        "
    echo "       -h --help   show this help message and exit "
}

if [[ $1 == "-h" ]] || [[ $1 == "--help" ]]; then
    help
    exit
fi

declare -A classes=( [trie]="Trie" [aho_corasick]="AhoCorasick" [hash_table]="HashTable" [regex]="Regex" [baseline]="Baseline" )
benchmarks=(insert memory phrases words)

if [ -n "$1" ] && [[ ! -v classes[${1}] ]] && [[ ! " ${benchmarks[*]} " =~ " ${1} " ]]; then
    echo "error: arg \`$1\` is neither a class nor a benchmark"
    echo
    help
    exit 1
fi

if [ -n "$2" ] && [[ ! " ${benchmarks[*]} " =~ " ${2} " ]]; then
    echo "error: arg \`$2\` is not a benchmark"
    echo
    echo "The current benchmarks are: ${benchmarks[*]}"
    echo
    echo "For more info, see: \`run-benchmark.sh --help\`"
    exit 1
fi

results_dir=out/results
scripts_dir=scripts/python

if [ ! -d $results_dir ] || [ ! -d out/graphs ]; then
    mkdir -p out/{results,graphs}
fi

if [ ! -f data/bad_words/formatted.csv ] || [ ! -f data/good_words/formatted.txt ]; then
    [[ ! -f data/bad_words/formatted.csv ]] && echo "info: file  \`data/bad_words/formatted.csv\` does not exist." 
    [[ ! -f data/good_words/formatted.txt ]] && echo "info: file \`data/good_words/formatted.txt\` does not exist." 
    python3 $scripts_dir/generate/format_wordlist.py
fi

if [ ! -d data/sentences ]; then
    python3 $scripts_dir/generate/sample_generator.py
fi

if [[ -v classes[${1}] ]]; then
    run_class=${classes[${1}]}
    run_package="${1}\."
    shift
else
    run_package=".*"
fi

if [ -n "$1" ]; then
    run_comp=$1
else
    run_all=yes
fi

mvn clean install
mvn clean package

# performance de inserção
if [[ $run_comp == insert ]] || [ -n "$run_all" ]; then
    java -jar target/benchmarks.jar "${run_package}${run_class}Benchmark\.insertAll" -rf csv -rff $results_dir/insertion_perf.csv -p currentTest=insert
fi

# consumo de memória 
if [[ $run_comp == memory ]] || [ -n "$run_all" ]; then
    java -jar target/benchmarks.jar "${run_package}${run_class}Benchmark\.insertAll" -prof gc -rf csv -rff $results_dir/memory_usage.csv -p currentTest=memory
fi

# velocidade de busca em frases e detecção
if [[ $run_comp == phrases ]] || [ -n "$run_all" ]; then
    if [ ! -f $results_dir/search_phrases_efficiency.csv ]; then
        echo "\"Benchmark\",\"Correct Count\",\"Missed\",\"Margin of Error\"\"Category\",\"Param: phraseSize\"" > $results_dir/search_phrases_efficiency.csv
    fi
    # java -jar target/benchmarks.jar "${run_package}${run_class}Benchmark\.searchPhrases" -rf csv -rff $results_dir/search_phrases_perf.csv -p currentTest=phrases -p phraseSize=1000,5000,10000
    java -jar target/benchmarks.jar "${run_package}${run_class}Benchmark\.searchPhrases" -p currentTest=phrases -p phraseSize=50
fi

# velocidade de busca em palavras e detecção
if [[ $run_comp == words ]] || [ -n "$run_all" ]; then
    if [ ! -f $results_dir/query_words_efficiency.csv ]; then
        echo "\"Benchmark\",\"Correct\",\"Wrong\",\"Category\"" > $results_dir/query_words_efficiency.csv
    fi
    java -jar target/benchmarks.jar "${run_package}${run_class}Benchmark\.queryWords" -rf csv -rff $results_dir/query_words_perf.csv -p currentTest=words
fi

# TODO: gerar gráficos, fazer após já ter os benchmarks configurados