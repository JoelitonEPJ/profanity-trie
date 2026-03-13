package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileUtils {

    private static final Path SENTENCES_DIR = Paths.get("data", "sentences");
    private static final Path RESULTS_DIR   = Paths.get("out",  "results");

    private static final Path GOOD_WORDS_FILE = Paths.get("data", "good_words", "formatted.txt");
    private static final Path BAD_WORDS_FILE  = Paths.get("data", "bad_words",  "formatted.csv");
    private static final Path LEET_CODES_FILE = Paths.get("data", "leet_codes.csv");

    public static String[] readFile(Path caminho) {
        return readFile(caminho.toString());
    }

    /**
     * Reads a text file and transforms its content into a String array
     * where each line is an elements
     *
     * @param caminho Path to the file.
     * @return An string array where each element was a line from the line
     * @throws IOException If an error happens when reading the file
     */
    public static String[] readFile(String caminho) {

        List<String> lista = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(caminho))) {
            String linha;
            while (true) {
                linha = bufferedReader.readLine();
                if (linha == null) {
                    break;
                }
                if (linha.isEmpty()) {
                    continue;
                }
                lista.add(linha);
            }
        } catch (IOException e) {
            System.err.println("error: unable to read file `" + caminho + "`, details: " + e.getMessage());
            return null;
        }

        return lista.toArray(new String[0]);
    }

    /**
     * Reads the file containing the bad words and returns its content
     * 
     * @return an array of pairs with the following structure { badWord, firstPrefix }
     */
    @SuppressWarnings("unchecked")
    public static Pair<String, Integer>[] readBadWords() {
        String[] linhas = readFile(BAD_WORDS_FILE);

        Pair<String, Integer>[] badWordsFirstPrefix = new Pair[linhas.length - 1];
        for (int i = 1; i < linhas.length; i++) {
            String[] badWordPrefix = linhas[i].split(",");
            badWordsFirstPrefix[i - 1] = new Pair(badWordPrefix[0], Integer.valueOf(badWordPrefix[1]));
        }

        return badWordsFirstPrefix;
    }

    /**
     * Reads the file containing the good words and returns its content
     * 
     * @return an array containing the formatted good words
     */
    public static String[] readGoodWords() {
        return readFile(GOOD_WORDS_FILE);
    }

    /**
     * Reads the csv file containing the char-leet relationship and returns its content as a HashMap
     * 
     * @return a HashMap containing the relationship { char=>[leets] }
     */
    public static Map<Character, String[]> readCsvCharToLeetMap() {

        String[] linhas = readFile(LEET_CODES_FILE);
        Map<Character, String[]> dicionario = new HashMap<>();

        String[] value;
        for (int i = 1; i < linhas.length; i++) {
            String[] colunas = linhas[i].split(",");
            Character chave = colunas[0].charAt(0);

            String[] valores = colunas[1].split(" ");
            if (colunas.length == 3) {
                value = Arrays.copyOf(valores, valores.length + 1);
                value[value.length - 1] = colunas[2];
            } else {
                value = Arrays.copyOf(valores, valores.length);
            }
            dicionario.put(chave, value);
        }

        return dicionario;
    }

    /**
     * Reads the csv file containing the char-leet relationship and returns its content as a HashMap
     * 
     * @return a HashMap containing the inversed relationship { leet=>[chars] }
     */
    public static Map<Character, ArrayList<Character>> readCsvLeetToCharMap() {

        String[] linhas = readFile(LEET_CODES_FILE);
        Map<Character, ArrayList<Character>> dicionario = new HashMap<>();

        for (int i = 1; i < linhas.length; i++) {
            String[] colunas = linhas[i].split(",");

            String[] chaves = colunas[1].split(" ");
            if (colunas.length == 3) {
                chaves = Arrays.copyOf(chaves, chaves.length + 1);
                chaves[chaves.length - 1] = colunas[2];
            }

            for (String chave : chaves) {
                Character leet = chave.charAt(0);
                dicionario.putIfAbsent(leet, new ArrayList<>());

                List<Character> value = dicionario.get(leet);
                if (!value.contains(colunas[0].charAt(0))) {
                    value.add(colunas[0].charAt(0));
                }
            }
        }

        return dicionario;
    }

    /**
     * Reads the csv file containing the phrases with the specified {@code phraseSize}
     * 
     * @return an array of pairs with the following structure { phrase, badWordsCount }
     */
    @SuppressWarnings("unchecked")
    public static Map<WordCategory, List<Pair<String, Integer>>> readPhrases(int phraseSize) {

        String arquivo = "frases_" + phraseSize + "_palavras.csv";
        String[] linhas = readFile(SENTENCES_DIR.resolve(arquivo));

        Map<WordCategory, List<Pair<String, Integer>>> phrases = new HashMap<>();
        for (int i = 1; i < linhas.length; i++) {
            String[] linha = linhas[i].split(",");
            WordCategory category = WordCategory.getCategory(linha[2]);

            phrases.putIfAbsent(category, new ArrayList<>());
            phrases.get(category).add(new Pair<>(linha[0], Integer.valueOf(linha[1])));
        }

        return phrases;
    }

    /**
     * Reads the csv file containing the words to query 
     * 
     * @return a HashMap with the following structure { category=>[words] }
     */
    public static Map<WordCategory, List<String>> readWords() {

        String[] linhas = readFile(SENTENCES_DIR.resolve("sample_words.csv"));
        Map<WordCategory, List<String>> words = new HashMap<>();

        for (int i = 1; i < linhas.length; i++) {
            String[] categorizedWords = linhas[i].split(",");
            WordCategory category = WordCategory.getCategory(categorizedWords[1]);

            words.putIfAbsent(category, new ArrayList<>());
            words.get(category).add(categorizedWords[0]);
        }

        return words;
    }

    /**
     * Saves (overriding, if necessary) the results of the new searchPhrases run.
     * 
     * @param className    name of the class to be recorded
     * @param correct      how many bad word counts were correct for this category
     * @param missed       how many bad word counts were incorrect for this category
     * @param errorMargin  the biggest margin of error for this category
     * @param phraseSize   the phrase size used for this comparison
     * @param category     the category to be saved
     */
    public static void savePhrasesResult(String className, int correct, int missed, int errorMargin, String category, int phraseSize) {
        final Path filePath = RESULTS_DIR.resolve("search_phrases_efficiency.csv");

        String lineToSave = className + "," + correct + "," + missed + "," + errorMargin + "," + category + "," + phraseSize;
        List<String> linhas = new ArrayList<>();
        if (Files.exists(filePath)) Collections.addAll(linhas, readFile(filePath));

        for (int i = 1; i < linhas.size(); i++) {
            String linha = linhas.get(i);
            if (linha.startsWith(className) && linha.contains(category) && linha.endsWith("," + phraseSize)) {
                linhas.set(i, lineToSave);

                saveFileContent(filePath, linhas);
                return;
            }
        }

        linhas.add(lineToSave);
        saveFileContent(filePath, linhas);
    }

    /**
     * Saves (overriding, if necessary) the results of the new queryWords run.
     * 
     * @param className  name of the class to be recorded
     * @param correct    how many bad words were correctly identified for this category
     * @param missed     how many bad words were not identified for this category
     * @param category   the category to be saved
     */
    public static void saveWordsResult(String className, int correct, int missed, String category) {
        final Path filePath = RESULTS_DIR.resolve("query_words_efficiency.csv");

        String lineToSave = className + "," + correct + "," + missed + "," + category;
        List<String> linhas = new ArrayList<>();
        if (Files.exists(filePath)) Collections.addAll(linhas, readFile(filePath));

        for (int i = 1; i < linhas.size(); i++) {
            if (linhas.get(i).startsWith(className) && linhas.get(i).endsWith(category)) {
                linhas.set(i, lineToSave);

                saveFileContent(filePath, linhas);
                return;
            }
        }

        linhas.add(lineToSave);
        saveFileContent(filePath, linhas);
    }

    /**
     * Saves the content to a file
     * 
     * @param caminho  path to the file
     * @param linhas   content to be saved on this file
     */
    public static void saveFileContent(Path caminho, List<String> linhas) {
        try {
            System.out.println("info: writing to file `" + caminho + "`");
            Files.write(caminho, linhas, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("error: unable to save file `" + caminho + "`, details: " + e.getMessage());
            System.exit(1);
        }
    }
}
