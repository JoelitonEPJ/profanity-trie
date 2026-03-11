from matplotlib.ticker import FuncFormatter
from pathlib import Path
from math import ceil

import matplotlib.pyplot as plt
import csv

ROOT_DIR = Path(__file__).parents[3]
RESULTS_DIR = ROOT_DIR/"out"/"results"
GRAPHS_DIR = ROOT_DIR/"out"/"graphs"
IDX_SCORE = 4

def identifica_algoritmo(linha):
    alg = linha.split(".")[0]

    match alg:
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

def insertion_memory_usage_benchmark_plot():
    x = []
    y = []

    with open(RESULTS_DIR/"insertion_perf.csv", "r") as csvfile:
        reader = csv.reader(csvfile, delimiter=",")
        next(reader)

        for linha in reader:
            if "norm" in linha[0]:
                x.append(identifica_algoritmo(linha[0]))
                y.append(float(linha[IDX_SCORE].replace(",", ".")) / (8 * 1024 * 1024))
        
        formatter = FuncFormatter(formata_mb)

        fig, ax = plt.subplots()

        ax.yaxis.set_major_formatter(formatter)

        ax.bar(x, y, width=0.5, color="royalblue", zorder=2)

        ax.set_yticks(range(0, 81, 20))
        ax.tick_params(axis="y", length=0)

        ax.spines[["top", "left", "right"]].set_visible(False)

        ax.set_xlabel("Algoritmo", color="gray", fontsize=13)
        ax.set_ylabel("Uso de Memória (MB/op)", color="gray", fontsize=13)
        ax.set_title("Insert All Memory Usage Benchmark", fontsize=16)

        ax.grid(color="gray", axis="y", linestyle="dotted", alpha=0.3, zorder=1)

        plt.savefig(GRAPHS_DIR/"insert_all_memory_usage.png", dpi=100, bbox_inches="tight")


def insertion_speed_benchmark_plot():
    x = []
    y = []

    with open(RESULTS_DIR/"insertion_perf.csv", "r") as csvfile:
        reader = csv.reader(csvfile, delimiter=",")
        next(reader)

        for linha in reader:
            if "rate" in linha[0] and "norm" not in linha[0]:
                x.append(identifica_algoritmo(linha[0]))
                y.append(float(linha[4].replace(",", ".")))

        formatter = FuncFormatter(formata_mb)
        
        fig, ax = plt.subplots()

        ax.yaxis.set_major_formatter(formatter)

        ax.bar(x, y, width=0.5, color="rebeccapurple", zorder=2)

        ax.set_yticks(range(0, 1201, 300))
        ax.tick_params(axis="y", length=0)

        ax.spines[["top", "left", "right"]].set_visible(False)

        ax.set_xlabel("Algoritmo", color="gray", fontsize=13)
        ax.set_ylabel("Memória Alocada (MB/s)", color="gray", fontsize=13)
        ax.set_title("Insertion Speed Benchmark", fontsize=16)

        ax.grid(color="gray", axis="y", linestyle="dotted", alpha=0.3, zorder=1)

        plt.savefig(GRAPHS_DIR/"insertion_speed_benchmark.png", dpi=100, bbox_inches="tight")



def formata_mb(num, pos):
    return f"{num:.0f}MB"


def word_query_classification_plot():
    x = []
    y = []

    with open(RESULTS_DIR/"word_query_benchmark.csv", "r") as csvfile:
        reader = csv.reader(csvfile, delimiter=",")
        next(reader)


if __name__ == "__main__":
    insertion_memory_usage_benchmark_plot()
    insertion_speed_benchmark_plot()