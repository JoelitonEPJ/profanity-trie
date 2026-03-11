package regex;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {
    
    private Pattern pattern;
    private Map<Character, String[]> leetMap;

    public Regex(String[] pattern, Map<Character, String[]> leetMap) {
        this.leetMap = leetMap;
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
        while (matcher.find()) {
            // System.out.println(matcher.group());
            counter++;
        }

        return counter;
    }

    public boolean matches(String word) {
        Matcher matcher = matcher(word);

        return matcher.matches();
    }

    private void escapePipe(String[] toEscape) {
        for (int i = 0; i < toEscape.length; i++) {
            if (toEscape[i].equals("|")) toEscape[i] = "\\\\|";
        }
    }

    public String buildPattern(String[] palavras) {
        StringBuilder padrao = new StringBuilder("\\s*\\b(");

        for (int i = 0; i < palavras.length; i++) {
            char ultimoChar = 0;
            String[] leets;

            for (int j = 0; j < palavras[i].length(); j++) {
                char charAtual = Character.toLowerCase(palavras[i].charAt(j));

                leets = leetMap.getOrDefault(charAtual, new String[] { "" + charAtual });
                escapePipe(leets);

                if (ultimoChar != charAtual) padrao.append("(?:").append(String.join("|", leets)).append(")+\\s*");

                ultimoChar = charAtual;
            }

            if (i != palavras.length - 1) padrao.append("|");
        }

        return padrao.append(")\\b\\s*").toString();
    }
}