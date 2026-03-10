from pathlib import Path
from sys import argv
import random
import csv
import os

ROOT_DIR = Path(__file__).parent.parent.parent

DATA_DIR = ROOT_DIR/"data"
GOOD_WORDS_DIR = DATA_DIR/"good_words"
BAD_WORDS_DIR = DATA_DIR/"bad_words"

TAM_FRASES_DEFAULT = [1000, 5000, 10000]
QUANT_FRASES_DEFAULT = 1000
SEED = 33550336

def gera_frases(tam_frases):
    random.seed(SEED)

    with open(GOOD_WORDS_DIR/"formatted.txt", "r", encoding="utf-8") as input:
        good_words = input.readlines()

    with open(BAD_WORDS_DIR/"formatted.csv", "r", encoding="utf-8") as input:
        bad_words = [linha[:linha.index(",")] for linha in input.readlines()[1:]]

    leet_dict = leetcsv_to_dict()
    for i in range(len(tam_frases)):
        tam_atual = tam_frases[i]

        os.makedirs(DATA_DIR/"sentences", exist_ok=True)

        arquivo = f"frases_{tam_atual}_palavras.csv"

        with open(DATA_DIR/"sentences"/arquivo, "w", newline="", encoding="utf-8") as arq_csv:
            writer = csv.writer(arq_csv, delimiter=",")
            writer.writerow(["frase", "qtd_bad_words", "modificador"])
            for _ in range(QUANT_FRASES_DEFAULT):
                mod = select_modifier() # lista de modificadores dessa frase

                frase_atual = ""
                conta_bad_words = 0
                for _ in range(tam_atual):
                    if random.random() < 0.75:
                        frase_atual += random.choice(good_words).strip() + " "
                    else:
                        frase_atual += modifier(random.choice(bad_words).strip(), leet_dict, mod).strip() + " "
                        conta_bad_words += 1

                writer.writerow([frase_atual.strip(), conta_bad_words, mod])

def select_modifier():
    out = []

    # chance encoder
    if random.random() < 0.2:
        out.append("encode")
    # chance stretcher
    if random.random() < 0.3:
        out.append("stretch")
    # chance uppercaser
    if random.random() < 0.5:
        out.append("uppercase")
    # chance spacer
    if random.random() < 0.05:
        out.append("space")

    return random.choice(out) if out else "none"

def stretcher(palavra):
    out = ""

    for letra in palavra:
        out += letra * random.randint(1, 7)

    return out

def spacer(palavra):
    out = ""

    for letra in palavra:
        out += letra + " "
    
    return out

def encoder(palavra, leet_dict):
    out = ""

    for letra in palavra:
        if not is_special_character(letra): 
            letra = random.choice(leet_dict[letra])
        
        out += letra

    return out

def uppercaser(palavra):
    out = ""

    for letra in palavra:
        out += letra.upper()
    
    return out

def modifier(palavra, leet_dict, modifier):
    out = ""

    for letra in palavra:
        if random.random() < 0.75:
            match modifier:
                case "encode":
                    letra = encoder(letra, leet_dict)
                case "stretch":
                    letra = stretcher(letra)
                case "space":
                    letra = spacer(letra)
                case "uppercase":
                    letra = uppercaser(letra)
        
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

# rode dessa maneira: python sentences_generator.py <numero de palavras desejado das frases>
# ex: python sentences_generator.py 100 200 300
if __name__ == "__main__":
    gera_frases([int(v) for v in argv[1:]] or TAM_FRASES_DEFAULT)