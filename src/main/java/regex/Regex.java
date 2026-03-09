package regex;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Map;
import util.FileUtils;

public class Regex {
    
    private Pattern pattern;
    private Map<Character, String[]> leetMap;

    public Regex(String[] pattern) {
        this.leetMap = FileUtils.readCsvCharToLeetMap();
        this.pattern = Pattern.compile(buildPattern(pattern), Pattern.CASE_INSENSITIVE);
    }

    public void setPattern(String pattern) {
        this.pattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
    }

    public Matcher matcher(String entrada) {
        return pattern.matcher(entrada);
    }

    public int countMatches(String entrada) {
        int counter = 0;

        Matcher matcher = matcher(entrada);
        while (matcher.find()) 
            counter++;

        return counter;
    }

    public boolean matches(String word) {
        Matcher matcher = matcher(word);

        return matcher.matches();
    }

    public String buildPattern(String[] palavras) {
        StringBuilder pattern = new StringBuilder();

        String padrao = "";
        for (int i = 0; i < palavras.length; i++) {
            char ultimoChar = 0;
            String[] leets = {};
            for (int j = 0; j < palavras[i].length(); j++) {
                char charAtual = Character.toLowerCase(palavras[i].charAt(j));
                leets = this.leetMap.get(charAtual);

                if (ultimoChar != charAtual) padrao = "(?:" + String.join("|", leets) + ")+ ?";

                ultimoChar = charAtual;
            }

            if (!(pattern.length() == 0)) pattern.append("|");
            pattern.append(padrao); 
        }

        return pattern.toString();
    }
}