from pathlib import Path
from re import sub

ROOT = Path(__file__).parent.parent.parent

def simple_normalize(content):
    texto = sub(r"(â|ã|á|à|4|@)", "a", content)
    texto = texto.replace("ç", "c").replace("í", "i").replace("ú", "u")
    texto = sub(r"(ê|é|3)", "e", texto)
    texto = sub(r"(ô|ó|õ|º|0)", "o", texto)

    return texto


def clean_words(content):
    splitted = set((simple_normalize(bad_word.lower()) for bad_word in content.split()))

    return sorted(splitted)

if __name__ == "__main__":
    DATA_DIR = ROOT/"data"

    with open(DATA_DIR/"bad_words_unformatted.txt", "r") as input:
        content = input.read()

    cleaned = clean_words(content)
    with open(DATA_DIR/"bad_words_formatted.txt", "w") as output:
        for word in cleaned:
            output.write(f"{word}\n")