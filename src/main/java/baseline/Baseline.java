package baseline;

import java.util.ArrayList;

import util.FileUtils;

public class Baseline {
    
    private String[] badWords;

    public Baseline(String[] badWords){
        this.badWords = badWords;
    }

    public boolean isBadWord(String palavra){
        for(int i = 0; i < this.badWords.length; i++){
            if(this.badWords[i].equals(palavra))
                return true;
        }
        return false;
    }

    public ArrayList<Integer> hasBadWord(String frase) {
        ArrayList<Integer> posicaoBadWords = new ArrayList<>();
        String[] entrada = frase.split(" ");

        for(int i = 0; i < entrada.length; i++){
            if(isBadWord(entrada[i])){
                posicaoBadWords.add(i);
            }
        }

        if(posicaoBadWords.isEmpty()) return null;
        return posicaoBadWords;
    }
}