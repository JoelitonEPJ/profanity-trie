package regex;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.ArrayList;

public class Regex {
    
    private Pattern pattern;

    public Regex(String pattern
        
    ) {
        this.pattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
    }

    public void setPattern(String pattern) {
        this.pattern = Pattern.compile(pattern);
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
}