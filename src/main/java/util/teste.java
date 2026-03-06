package util;

import java.util.Map;
import java.util.Arrays;

import util.FileUtils;

public class teste {

    public static void main(String[] args) {
        Map<Character, Character[]> dicionario = FileUtils.readLeetCodes();

    for (Map.Entry<Character, Character[]> entry : dicionario.entrySet()) {
        System.out.println(entry.getKey() + " -> " + Arrays.toString(entry.getValue()));
    }
    }
}
