from pathlib import Path

import matplotlib.pyplot as plt
import csv

ROOT_DIR = Path("README.md").parent
RESULTS_DIR = ROOT_DIR/"out"/"results"
GRAPHS_DIR = ROOT_DIR/"out"/"graphs"
IDX_SCORE = 4

def identifica_algoritmo(linha):
    line = linha.split(".")[0]

    match line:
        case "aho_corasick":
            return "Aho Corasick"
        case "baseline":
            return "Baseline"
        case "hash_table":
            return "Hash Table"
        case "regex":
            return "Regex"
        case "trie":
            return "Trie"

def insertion_perf_memory_usage():
    x = []
    y = []

    with open(RESULTS_DIR/"insertion_perf.csv", "r") as csvfile:
        reader = csv.reader(csvfile, delimiter=",")
        next(reader)

        for linha in reader:
            if "gc.alloc.rate.norm" in linha[0]:
                x.append(identifica_algoritmo(linha[0]))
                y.append(float(linha[IDX_SCORE].replace(",", ".")) / (8 * 1024 * 1024))
        
        plt.bar(x, y, width=0.5, color="darkblue")
        plt.yticks(range(0, 81, 20), alpha=0.75)
        plt.xticks(alpha=0.75)
        plt.box(False)
        plt.xlabel("Algoritmo", color="gray", fontsize=13)
        plt.ylabel("Uso de Memória (MB/op)", color="gray", fontsize=13)
        plt.title("Insert All Memory Usage Benchmark", fontsize=16)
        plt.grid(color="gray", axis="y", linestyle="dotted", alpha=0.3, zorder=1)
        plt.savefig(GRAPHS_DIR/"insert_all_plot.png", dpi=300)
        

        # fig, ax = plt.subplots()



if __name__ == "__main__":
    insertion_perf_memory_usage()