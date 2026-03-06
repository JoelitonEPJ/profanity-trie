package baseline;

import java.util.ArrayList;

import util.FileUtils;

public class Baseline {
    
    private String[] bad_words;

    public Baseline(){
        this.bad_words = FileUtils.readBadWords();
    }

    public boolean isBadWord(String palavra){
        for(int i = 0; i < this.bad_words.length; i++){
            if(this.bad_words[i].equals(palavra))
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