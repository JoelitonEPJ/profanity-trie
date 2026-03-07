package util;

import java.util.List;
import java.util.Map;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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

    // public static Map<Character, String[]> readCsvCharToLeetMap(String caminho){

    //     Map<Character, String[]> dicionario = new HashMap<>();

    //     try(BufferedReader bufferedReader = new BufferedReader(new FileReader(caminho))){

    //         bufferedReader.readLine(); // pula o cabeçalho

    //         String linha;
    //         while(true){
    //             linha = bufferedReader.readLine();
    //             if(linha == null) break;

    //             String[] colunas = linha.split(",");
    //             Character chave = colunas[0].charAt(0);
    //             String[] valores = colunas[1].split(" ");

    //             String[] value;
    //             if(colunas.length >= 3){
    //                 value = Arrays.copyOf(valores, valores.length + 1);
    //                 value[value.length - 1] = colunas[2];
    //             } else {
    //                 value = Arrays.copyOf(valores, valores.length);
    //             }
    //             dicionario.put(chave, value);
    //         }
    //     } catch (IOException e) {
    //         System.err.println("Erro ao ler o arquivo: " + e.getMessage());
    //         return null;
    //     }

    //     return dicionario;
    // }

    public static Map<Character, String[]> readCsvCharToLeetMap(){

        String[] linhas = readFile(LEET_CODES);
        Map<Character, String[]> dicionario = new HashMap<>();

        String[] value;
        for(int i = 1; i < linhas.length; i++){
            String[] colunas = linhas[i].split(",");

            Character chave = colunas[0].charAt(0);
            String[] valores = colunas[1].split(" ");
            
            if(colunas.length == 3){
                value = Arrays.copyOf(valores, valores.length + 1);
                value[value.length - 1] = colunas[2];
            } else {
                value = Arrays.copyOf(valores, valores.length);
            }

            dicionario.put(chave, value);
        }

        return dicionario;
    }

    // public static Map<Character, char[]> readCsvLeetToCharMap(String caminho){
    
    //     Map<Character, char[]> dicionario = new HashMap<>();

    //     try(BufferedReader bufferedReader = new BufferedReader(new FileReader(caminho))){
            
    //         bufferedReader.readLine(); // pula o cabeçalho

    //         String linha;
    //         while(true){
    //             linha = bufferedReader.readLine();
    //             if(linha == null) break;

    //             String[] colunas = linha.split(",");
    //             String[] leets = colunas[1].split(" ");

    //             for(String caractere: leets){

    //                 Character chave = caractere.charAt(0);
    //                 char[] value;

    //                 if(colunas.length >= 3){
    //                     value = new char[2];
    //                     value[0] = colunas[0].charAt(0);
    //                     value[1] = colunas[2].charAt(0);
    //                 } else {
    //                     value = new char[]{colunas[0].charAt(0)};
    //                 }
    //                 dicionario.put(chave, value);
    //             }
    //         }
    //     } catch (IOException e) {
    //         System.err.println("Erro ao ler o arquivo: " + e.getMessage());
    //         return null;
    //     }

    //     return dicionario;
    // }

    public static Map<Character, char[]> readCsvLeetToCharMap(){ 

        String[] linhas = readFile(LEET_CODES);
        Map<Character, char[]> dicionario = new HashMap<>();

        char[] value;
        for(int i = 1; i < linhas.length; i++){
            String[] colunas = linhas[i].split(",");

            String[] chaves = colunas[1].split(" ");

            for(int j = 0; j < chaves.length; j++){
                Character chave = chaves[j].charAt(0);
                if(colunas.length == 3){
                    value = new char[2];
                    value[0] = colunas[0].charAt(0);
                    value[1] = colunas[2].charAt(0);
                } else {
                    value = new char[]{colunas[0].charAt(0)};
                }

                dicionario.put(chave, value);
            }
        }

        return dicionario;
    }
}
