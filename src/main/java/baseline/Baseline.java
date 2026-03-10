package baseline;

import java.util.Arrays;

public class Baseline {
    
    private String[] badWords;

    public Baseline(String[] badWords){
        this.badWords = Arrays.copyOf(badWords, badWords.length);
    }

    public boolean isBadWord(String palavra){
        for(int i = 0; i < this.badWords.length; i++){
            if(this.badWords[i].equals(palavra))
                return true;
        }
        return false;
    }

    public int countBadWords(String frase) {
        int count = 0;
        String[] entrada = frase.split(" ");

        for(int i = 0; i < entrada.length; i++){
            if(isBadWord(entrada[i])){
                count++;
            }
        }

        return count;
    }
}