from pathlib import Path
from sys import argv
import random
import csv
import os

ROOT_DIR = Path(__file__).parents[3]

DATA_DIR = ROOT_DIR/"data"
GOOD_WORDS_DIR = DATA_DIR/"good_words"
BAD_WORDS_DIR = DATA_DIR/"bad_words"
SENTENCES_DIR = DATA_DIR/"sentences"

CATEGORIES = ["good_word", "none", "upper", "spaced", "encoded", "stretched"]
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
    print(f"info: Generating sample sentences at `{SENTENCES_DIR}`...")

    good_words, bad_words = get_words()
    os.makedirs(SENTENCES_DIR, exist_ok=True)

    leet_dict = leetcsv_to_dict()
    for tamanho in TAM_FRASES_DEFAULT:
        category_quant = {category: 0 for category in CATEGORIES}
        arquivo = SENTENCES_DIR/f"frases_{tamanho}_palavras.csv"

        with open(arquivo, "w", newline="", encoding="utf-8") as arq_csv:
            writer = csv.writer(arq_csv, delimiter=",")
            writer.writerow(["frase", "qtd_bad_words", "category"])
            for _ in range(QUANT_LINHAS_DEFAULT):
                category = random.choice(CATEGORIES)
                category_quant[category] += 1

                frase_atual = ""
                conta_bad_words = 0
                for _ in range(tamanho):
                    if category == "good_word" or random.random() < 0.75:
                        frase_atual += random.choice(good_words).strip() + " "
                    else:
                        if category == "none":
                            frase_atual += random.choice(bad_words).strip() + " "
                        else:
                            frase_atual += modifier(random.choice(bad_words).strip(), leet_dict, category).strip() + " "

                        conta_bad_words += 1

                writer.writerow([frase_atual.strip(), conta_bad_words, category])

        print(f"info: File `{arquivo}` was saved successfully!")
        for category, quant in category_quant.items():
            pad_amount = len(CATEGORIES[0]) - len(category)
            print(f"    info: {quant} samples for category `{category}`{'':<{pad_amount}} were generated")

def gera_palavras():
    print(f"info: Generating sample words at `{SENTENCES_DIR}`...")

    _, bad_words = get_words(good=False)
    os.makedirs(SENTENCES_DIR, exist_ok=True)

    leet_dict = leetcsv_to_dict()
    arquivo = SENTENCES_DIR/"sample_words.csv"
    with open(arquivo, "w", newline="", encoding="utf-8") as arq_csv:
        category_quant = {category: 0 for category in CATEGORIES}
        writer = csv.writer(arq_csv, delimiter=",")

        writer.writerow(["palavra", "modificador"])
        for _ in range(QUANT_LINHAS_DEFAULT*10):
            category = random.choice(CATEGORIES)
            category_quant[category] += 1

            word = random.choice(bad_words).strip()
            if category == "good_word":
                word = random.choice(hidden_bad_words).strip()
            elif category != "none":
                word = modifier(word, leet_dict, category).strip()

            writer.writerow([word, category])
        
    print(f"info: File `{arquivo}` was saved sucessfully!")
    for category, quant in category_quant.items():
        pad_amount = len(CATEGORIES[0]) - len(category)
        print(f"    info: {quant} samples for category `{category}`{'':<{pad_amount}} were generated")
    


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