from pathlib import Path
from sys import argv
import random
import csv
import os

ROOT_DIR = Path(__file__).parents[3]

DATA_DIR = ROOT_DIR/"data"
GOOD_WORDS_DIR = DATA_DIR/"good_words"
BAD_WORDS_DIR = DATA_DIR/"bad_words"

MODIFIERS = ["hidden", "none", "upper", "spaced", "encoded", "stretched"]
TAM_FRASES_DEFAULT = [1000, 5000, 10000]
QUANT_LINHAS_DEFAULT = 1000
SEED = 33550336

hidden_bad_words = []

def get_words(good=True):
    if good:
        with open(GOOD_WORDS_DIR/"formatted.txt", "r", encoding="utf-8") as input:
            good_words = input.readlines()
    else:
        good_words = []

    with open(BAD_WORDS_DIR/"formatted.csv", "r", encoding="utf-8") as input:
        bad_words = [linha[:linha.index(",")] for linha in input.readlines()[1:]]

    return good_words, bad_words

def gera_frases():
    good_words, bad_words = get_words()
    os.makedirs(DATA_DIR/"sentences", exist_ok=True)

    leet_dict = leetcsv_to_dict()
    for tamanho in TAM_FRASES_DEFAULT:

        arquivo = f"frases_{tamanho}_palavras.csv"

        with open(DATA_DIR/"sentences"/arquivo, "w", newline="", encoding="utf-8") as arq_csv:
            writer = csv.writer(arq_csv, delimiter=",")
            writer.writerow(["frase", "qtd_bad_words", "modificador"])
            for _ in range(QUANT_LINHAS_DEFAULT):
                mod = select_modifier()

                frase_atual = ""
                conta_bad_words = 0
                for _ in range(tamanho):
                    if random.random() < 0.75:
                        frase_atual += random.choice(good_words).strip() + " "
                    else:
                        if mod == "none":
                            frase_atual += random.choice(bad_words).strip()
                        else:
                            frase_atual += modifier(random.choice(bad_words).strip(), leet_dict, mod).strip() + " "
                        conta_bad_words += 1

                writer.writerow([frase_atual.strip(), conta_bad_words, mod])

def gera_palavras():
    _, bad_words = get_words(good=False)

    leet_dict = leetcsv_to_dict()
    with open(DATA_DIR/"sentences"/"sample_words.csv", "w", newline="", encoding="utf-8") as arq_csv:
        writer = csv.writer(arq_csv, delimiter=",")
        writer.writerow(["palavra", "modificador"])
        for _ in range(QUANT_LINHAS_DEFAULT):
            mod = select_modifier(hidden=True)

            word = random.choice(bad_words).strip()
            if mod == "hidden":
                word = random.choice(hidden_bad_words).strip()
            elif mod != "none":
                word = modifier(word, leet_dict, mod).strip()

            writer.writerow([word, mod])

def select_modifier(hidden=False):
    modificadores = MODIFIERS if hidden else MODIFIERS[1:]
    return random.choice(modificadores)

def stretcher(palavra):
    out = ""

    for letra in palavra:
        out += letra * random.randint(1, 7)

    return out

def encoder(letra, leet_dict):
    if not is_special_character(letra): 
        return random.choice(leet_dict[letra])

    return letra

def modifier(palavra, leet_dict, modifier):
    out = ""

    for letra in palavra:
        if random.random() < 0.75:
            match modifier:
                case "encoded":
                    out += encoder(letra, leet_dict)
                case "stretched":
                    out += letra * random.randint(1, 7)
                case "spaced":
                    out += letra + " "
                case "upper":
                    out += letra.upper()
        else:
            out += letra

    return out
                

def is_special_character(character):
    return not (65 <= ord(character) <= 90) and not (97 <= ord(character) <= 122) and not (192 <= ord(character) <= 252)

def leetcsv_to_dict():
    dict = {}
    with open(ROOT_DIR/"data"/"leet_codes.csv", "r", encoding="utf-8") as csvfile:
        reader = csv.reader(csvfile)
        for linha in reader:
            if linha[0] != "char":
                dict[linha[0]] = linha[1].split()
                if len(linha) == 3:
                    dict[linha[0]].append(linha[2])

    return dict

def get_hidden_bad_words():
    good_words, bad_words = get_words()

    hidden_bad_words = set()
    for bad_word in bad_words:
        hidden_bad_words.update(filter(lambda good_word: bad_word in good_word, good_words))
    
    return sorted(hidden_bad_words)

# rode dessa maneira: python sample_generator.py [sentences|words]
if __name__ == "__main__":
    random.seed(SEED)
    generate = argv[1] if len(argv) > 1 else "all"

    if generate != "words":
        gera_frases()
    if generate != "sentences":
        hidden_bad_words = get_hidden_bad_words()
        gera_palavras()