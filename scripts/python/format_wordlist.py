from itertools import groupby
from collections import deque
from os.path import isfile
from pathlib import Path
from sys import argv
from re import sub

ROOT_DIR = Path(__file__).parent.parent.parent

GOOD_WORDS_DIR = ROOT_DIR/"data"/"good_words"
BAD_WORDS_DIR  = ROOT_DIR/"data"/"bad_words"


def normalize(content):
    texto = sub(r"(â|ã|á|à|ä|4|@)", "a", content)
    texto = sub(r"(ó|õ|ô|ò|ö|º|0)", "o", texto)
    texto = sub(r"(é|ẽ|ê|è|ë|3)",   "e", texto)
    texto = sub(r"(í|ĩ|î|ì|ï)",     "i", texto)
    texto = sub(r"(û|ũ|ú|ù|ü)",     "u", texto)
    texto = texto.replace("ç", "c")

    return texto


def clean_wordlist(content):
    return sorted(set((normalize(word.lower()) for word in content.split())))


def save_bad_words():
    with open(BAD_WORDS_DIR/"unformatted.txt", "r") as input:
        content = input.read()

    cleaned = clean_wordlist(content)
    with open(BAD_WORDS_DIR/"formatted.txt", "w") as output:
        for word in cleaned:
            output.write(f"{word}\n")


def remove_words_by_prefix(content):
    cleaned   = clean_wordlist(content)

    seen  = []
    index = 1
    while cleaned:
        grouped = groupby(cleaned, key=lambda word: word[:index])

        placeholder = []
        for curr_state, words in grouped:
            wordlist = deque(words)

            if curr_state in wordlist:
                assert curr_state == wordlist.popleft()
                if not wordlist:
                    seen.append(curr_state)

            placeholder.extend(wordlist)

        cleaned    = placeholder
        index += 1

    return set(seen)


def save_good_words():
    bw_filepath = BAD_WORDS_DIR/"formatted.txt"

    with open(GOOD_WORDS_DIR/"unformatted.txt", encoding="utf-8") as input:
        content = input.read()

    words = remove_words_by_prefix(content)

    if not isfile(bw_filepath):
        save_bad_words()

    with open(bw_filepath) as bw_file:
        bad_words = set(bw_file.read().split())

    good_words = words - bad_words
    with open(GOOD_WORDS_DIR/"formatted.txt", "w") as output:
        for good_word in sorted(good_words):
            output.write(f"{good_word}\n")


# rode da seguinte maneira: python format_wordlist.py [good|bad]
if __name__ == "__main__":
    save_option = argv[1] if len(argv) > 1 else "all"

    if save_option != "good":
        save_bad_words()
    if save_option != "bad":
        save_good_words()
