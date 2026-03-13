from matplotlib.ticker import FuncFormatter
from pathlib import Path
from numpy import arange

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

    with open(RESULTS_DIR/"memory_usage.csv", "r") as csvfile:
        reader = csv.reader(csvfile, delimiter=",")
        next(reader)

        for linha in reader:
            if "norm" in linha[0] and 'regex' not in linha[0]:
                x.append(identifica_algoritmo(linha[0]))
                y.append(float(linha[IDX_SCORE].replace(",", ".")) / (8 * 1024 * 1024))
        
    formatter = FuncFormatter(formata_mb)

    fig, ax = plt.subplots()

    ax.yaxis.set_major_formatter(formatter)

    ax.bar(x, y, width=0.5, color="royalblue", zorder=2)

    ax.tick_params(axis="y", length=0)

    ax.spines[["top", "left", "right"]].set_visible(False)

    ax.set_xlabel("Algorithm", color="gray", fontsize=13)
    ax.set_ylabel("Memory Usage (MB/op)", color="gray", fontsize=13)
    ax.set_title("Insert All Memory Usage Benchmark", fontsize=16, y=1.02, x=0.435)

    ax.grid(color="gray", axis="y", linestyle="dotted", alpha=0.3, zorder=1)

    plt.savefig(GRAPHS_DIR/"insert_all_memory_usage_without_regex.png", dpi=100, bbox_inches="tight")


def insertion_speed_benchmark_plot():
    x = []
    y = []

    with open(RESULTS_DIR/"insertion_perf.csv", "r") as csvfile:
        reader = csv.reader(csvfile, delimiter=",")
        next(reader)

        for linha in reader:
            x.append(identifica_algoritmo(linha[0]))
            y.append(float(linha[IDX_SCORE].replace(",", ".")) / 10 ** 6)

    formatter = FuncFormatter(formata_ms)
    
    fig, ax = plt.subplots()

    ax.yaxis.set_major_formatter(formatter)

    ax.bar(x, y, width=0.5, color="rebeccapurple", zorder=2)

    ax.tick_params(axis="y", length=0)

    ax.spines[["top", "left", "right"]].set_visible(False)

    ax.set_xlabel("Algorithm", color="gray", fontsize=13)
    ax.set_ylabel("Time (ms)", color="gray", fontsize=13)
    ax.set_title("Insertion Speed Benchmark", fontsize=16, x=0.42)

    ax.grid(color="gray", axis="y", linestyle="dotted", alpha=0.3, zorder=1)

    plt.savefig(GRAPHS_DIR/"insert_all_speed_benchmark.png", dpi=100, bbox_inches="tight")


def search_phrases_speed_plot():
    test_sizes = ["1000 words", "5000 words", "10000 words"]

    results = {}
    with open(RESULTS_DIR/"search_phrases_perf.csv", "r") as csvfile:
        reader = csv.reader(csvfile, delimiter=",")
        next(reader)

        for linha in reader:
            alg = identifica_algoritmo(linha[0])

            if not alg in results: 
                results[alg] = []
            
            results[alg].append(float((linha[IDX_SCORE].replace(",", "."))) / 10 ** 9)

    formatter = FuncFormatter(formata_secs)

    fig, ax = plt.subplots()

    ax.yaxis.set_major_formatter(formatter)

    idx = 0
    width = 0.15
    grupos = {}
    cores = ["midnightblue", "mediumblue", "royalblue", "dodgerblue", "deepskyblue"]
    x = arange(3)
    for key, value in results.items():
        if key != "Regex":
            offset = width * idx

            if not key in grupos:
                grupos[key] = []

            grupos[key] = ax.bar(x + offset, value, width=width, label=key, color=cores[idx], zorder=2)

            idx += 1
    
    ax.tick_params(axis="y", length=0)

    ax.spines[["top", "left", "right"]].set_visible(False)

    ax.set_xlabel("Sample Size", color="gray", fontsize=13, x=0.47)
    ax.set_ylabel("Time (secs)", color="gray", fontsize=13)
    ax.set_title("Search Phrases Time Benchmark", fontsize=16, x=0.42, y=1.10)

    ax.set_xticks(x + width, test_sizes)

    ax.legend(ncols=4, fontsize="small", frameon=False, loc="upper center", bbox_to_anchor=(0.45, 1.10))

    ax.grid(color="gray", axis="y", linestyle="dotted", alpha=0.3, zorder=1)

    plt.savefig(GRAPHS_DIR/"search_phrases_speed_benchmark.png", dpi=100, bbox_inches="tight")

    # incluindo regex

    formatter = FuncFormatter(formata_mins)
    ax.yaxis.set_major_formatter(formatter)

    grupos["Regex"] = ax.bar(x + offset, results["Regex"], width=width, label=key, color=cores[idx], zorder=2)

    ax.set_ylabel("Time (mins)", color="gray", fontsize=13)
    ax.set_title("Search Phrases Time Benchmark", fontsize=16, x=0.45, y=1.10)

    ax.legend(handles=grupos.values(), labels=grupos.keys(), ncols=5, fontsize="small", frameon=False, loc="upper center", bbox_to_anchor=(0.45, 1.10))
    
    plt.savefig(GRAPHS_DIR/"search_phrases_speed_benchmark_with_regex.png", dpi=100, bbox_inches="tight")


def formata_mb(num, pos):
    return f"{num:.0f}MB"


def formata_ms(num, pos):
    return f"{num:.0f}ms"


def formata_secs(num, pos):
    if int(num) == 1:
        return f"{num:.0f}sec"
    
    return f"{num:.0f}secs"


def formata_mins(num, pos):
    mins = num / 60

    if round(mins) == 1:
        return f"{mins:.0f}min"
    
    return f"{mins:.0f}mins"


if __name__ == "__main__":
    # insertion_memory_usage_benchmark_plot()
    # insertion_speed_benchmark_plot()
    search_phrases_speed_plot()