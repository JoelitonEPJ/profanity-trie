from collections import deque
from itertools import groupby
from os.path import isfile
from pathlib import Path
from csv import writer
from sys import argv
from re import sub

ROOT_DIR = Path(__file__).parents[3]

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


def clean_wordlist(content, ordered=True):
    cleaned = set((normalize(word.lower()) for word in content.split()))

    return sorted(cleaned) if ordered else cleaned


def assured_bad_word_prefix(good_words, bad_words):
    print("info: Calculating the first assured bad word prefix...")
    first_prefix_bad_word = {}

    for bad_word in bad_words:
        for i in range(len(bad_word)):
            if all(not good_word.startswith(bad_word[:i + 1]) for good_word in good_words):
                first_prefix_bad_word[bad_word] = i
                break
        else:
            # só será bad word se parar no fim da palavra
            first_prefix_bad_word[bad_word] = -1

    return first_prefix_bad_word


def save_bad_words():
    unf_filepath = BAD_WORDS_DIR/"unformatted.txt"
    gw_filepath  = GOOD_WORDS_DIR/"formatted.txt"
    bw_filepath  = BAD_WORDS_DIR/"formatted.csv"

    print(f"info: Generating file `{bw_filepath}` from `{unf_filepath}`...")
    with open(unf_filepath, encoding="utf-8") as input:
        bad_words = clean_wordlist(input.read())

    if not isfile(gw_filepath):
        print(f"info: File `{gw_filepath}` does not exist, creating...")
        save_good_words()

    with open(gw_filepath, encoding="utf-8") as gw_file:
        good_words = set(good_word for good_word in gw_file.read().split())

    bad_word_by_prefix = assured_bad_word_prefix(good_words, bad_words)
    with open(bw_filepath, "w", newline="", encoding="utf-8") as output:
        out_writer = writer(output, delimiter=",")
        out_writer.writerow(["word", "first_idx"])

        for word, first_idx in bad_word_by_prefix.items():
            out_writer.writerow([word, first_idx])

    print(f"info: File `{bw_filepath}` saved!")


def remove_words_by_prefix(content):
    print("info: Filtering out good words by its prefixes...")
    cleaned = clean_wordlist(content)

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

        cleaned  = placeholder
        index   += 1

    return set(seen)


def remove_variations(words, bad_words):
    print("info: Removing bad words' variations...")
    good_words = words - bad_words

    for bad_word in bad_words:
        good_words.discard(bad_word + 'es')
        good_words.discard(bad_word + 'as')
        good_words.discard(bad_word + 's')

        if bad_word.endswith('a') and not bad_word.endswith('inha') and not bad_word.endswith('ona'):
            good_words.discard(bad_word[:-1] + 'inha')
            good_words.discard(bad_word[:-1] + 'ona')
        if bad_word.endswith('o') and not bad_word.endswith('inho') and not bad_word.endswith('ao'):
            good_words.discard(bad_word[:-1] + 'inho')
            good_words.discard(bad_word[:-1] + 'ao')

    return good_words


def save_good_words():
    unf_filepath = GOOD_WORDS_DIR/"unformatted.txt"
    gw_filepath  = GOOD_WORDS_DIR/"formatted.txt"

    print(f"info: Generating file `{gw_filepath}` from `{unf_filepath}`...")
    with open(unf_filepath, encoding="utf-8") as input:
        content = input.read()

    words = remove_words_by_prefix(content)

    with open(BAD_WORDS_DIR/"unformatted.txt", encoding="utf-8") as bw_file:
        bad_words = clean_wordlist(bw_file.read(), ordered=False)

    good_words = remove_variations(words, bad_words)
    with open(gw_filepath, "w", encoding="utf-8") as output:
        for good_word in sorted(good_words):
            output.write(f"{good_word}\n")

    print(f"info: File `{gw_filepath}` saved!")


# rode da seguinte maneira: python format_wordlist.py [good bad]
if __name__ == "__main__":
    save_option = argv[1] if len(argv) > 1 else "all"

    if save_option != "bad":
        save_good_words()
    if save_option != "good":
        save_bad_words()
