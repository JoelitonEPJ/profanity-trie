package regex;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.ArrayList;
import java.util.HashMap;

public class Regex {
    
    private Pattern pattern;

    public Regex(String pattern) {
        this.pattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
    }

    public Regex(String[] palavras, HashMap<Character, String[]> leets) {
        buildPattern(palavras, leets);
    }

    public void setPattern(String pattern) {
        this.pattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
    }

    public Matcher matcher(String entrada) {
        return pattern.matcher(entrada);
    }

    public ArrayList<String> listarMatches(String entrada) {
        ArrayList<String> matches = new ArrayList<>();

        Matcher matcher = matcher(entrada);
        while (matcher.find()) {
            matches.add(matcher.group());
        }

        return matches;
    }

    public void buildPattern(String[] palavras, HashMap<Character, String[]> leetMap) {
        StringBuilder pattern = new StringBuilder();

        String padrao = "";
        for (int i = 0; i < palavras.length; i++) {
            char ultimoChar = 0;
            String[] leets = {};
            for (int j = 0; j < palavras[i].length(); j++) {
                char charAtual = Character.toLowerCase(palavras[i].charAt(j));
                leets = leetMap.get(charAtual);

                if (ultimoChar != charAtual) padrao = "(?:" + String.join("|", leets) + ")+";

                ultimoChar = charAtual;
            }

            if (!(pattern.length() == 0)) pattern.append("|");
            pattern.append(padrao); 
        }

        this.pattern = Pattern.compile(pattern.toString(), Pattern.CASE_INSENSITIVE);
    }
}