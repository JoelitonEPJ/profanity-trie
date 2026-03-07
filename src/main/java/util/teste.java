package util;

import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;

import util.FileUtils;

public class teste {

    public static void main(String[] args) {
        Map<Character, ArrayList<Character>> dicionario = FileUtils.readCsvLeetToCharMap();


        for (Map.Entry<Character, ArrayList<Character>> entry : dicionario.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}
