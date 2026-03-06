package util;

import java.util.List;
import java.util.Map;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class FileUtils {

    private static final String BAD_WORDS_FILE = "data/bad_words/formatted.txt";
    private static final String GOOD_WORDS_FILE = "data/good_words/formatted.txt";
    private static final String LEET_CODES = "data/leet_codes.csv";

    /**
     * Lê um arquivo de texto .txt e o transforma em um array de Strings em que cada linha é um elemento.
     *
     * @param caminhoArquivo O caminho do arquivo de texto a ser lido.
     * @return Um array onde cada elemento corresponde a uma linha do arquivo.
     * @throws IOException Se ocorrer algum erro durante a leitura do arquivo.
     */
    public static String[] readFile(String caminho){

        List<String> lista = new ArrayList<>();

        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(caminho))){
            String linha;
            while(true){
                linha = bufferedReader.readLine();
                if(linha == null) break;
                lista.add(linha);
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
            return null;
        }

        return lista.toArray(new String[0]);
    }

    public static String[] readBadWords() {
        return readFile(BAD_WORDS_FILE);
    }

    public static String[] readGoodWords() {
        return readFile(GOOD_WORDS_FILE);
    }

    public static Map<Character, Character[]> readCsv(String caminho){

        Map<Character, Character[]> dicionario = new HashMap<>();

        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(caminho))){
            String linha;
            while(true){
                linha = bufferedReader.readLine();
                if(linha == null) break;

                String[] colunas = linha.split(",");
                Character chave = colunas[0].charAt(0);
                String[] valores = colunas[1].split(" ");
                Character[] valoresChar = new Character[valores.length];

                for(int i = 0; i < valores.length; i++){
                    valoresChar[i] = valores[i].charAt(0);
                }

                dicionario.put(chave, valoresChar);
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
            return null;
        }

        return dicionario;
    }

    public static Map<Character, Character[]> readLeetCodes() {
        return readCsv(LEET_CODES);
    }
}
