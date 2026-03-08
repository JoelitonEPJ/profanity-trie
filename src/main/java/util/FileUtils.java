package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileUtils {

    private static final String RESULTS_DIR = "out/results/";

    private static final String BAD_WORDS_FILE = "data/bad_words/formatted.txt";
    private static final String GOOD_WORDS_FILE = "data/good_words/formatted.txt";
    private static final String LEET_CODES = "data/leet_codes.csv";

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
                lista.add(linha);
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
            return null;
        }

        return lista.toArray(new String[0]);
    }

    /**
     * Reads the file containing the bad words and returns its content
     * 
     * @return an array containing the formatted bad words
     */
    public static String[] readBadWords() {
        return readFile(BAD_WORDS_FILE);
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
     * @return a HashMap containing the relationship { char=>[leet] }
     */
    public static Map<Character, String[]> readCsvCharToLeetMap() {

        String[] linhas = readFile(LEET_CODES);
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
     * @return a HashMap containing the inversed relationship { leet=>[char] }
     */
    public static Map<Character, ArrayList<Character>> readCsvLeetToCharMap() {

        String[] linhas = readFile(LEET_CODES);
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

                ArrayList<Character> value = dicionario.get(leet);
                if (!value.contains(colunas[0].charAt(0))) {
                    value.add(colunas[0].charAt(0));
                }
            }
        }

        return dicionario;
    }

    /**
     * Saves (overriding, if necessary) the results of the new searchPhrases run.
     * 
     * @param className      name of the class to be recorded
     * @param correctAmount  how many bad_words_count were correct
     * @param phraseSize     the phrase size used for this comparison
     */
    public static void savePhrasesResult(String className, int correctAmount, int phraseSize) {
        final String filePath = Paths.get(RESULTS_DIR, "query_words_efficiency.csv").toString();

        String lineToSave = className + "," + correctAmount + "," + phraseSize;
        String[] linhas = readFile(filePath);

        boolean hasLinha = false;
        for (int i = 1; i < linhas.length; i++) {
            if (linhas[i].startsWith(className) && linhas[i].endsWith("," + phraseSize)) {
                linhas[i] = lineToSave;
                hasLinha = true;
                break;
            }
        }

        if (!hasLinha) {
            linhas = Arrays.copyOf(linhas, linhas.length + 1);
            linhas[linhas.length - 1] = lineToSave;
        }

        saveFileContent(filePath, linhas);
    }

    /**
     * Saves the content to a file
     * 
     * @param caminho  path to the file
     * @param linhas   content to be saved on this file
     */
    public static void saveFileContent(String caminho, String[] linhas) {
        try {
            List<String> conteudo = Arrays.asList(linhas);
            Path caminhoArquivo = Paths.get(caminho);

            Files.write(caminhoArquivo, conteudo, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("Erro ao salvar o arquivo: " + caminho);
            System.exit(1);
        }
    }
}
