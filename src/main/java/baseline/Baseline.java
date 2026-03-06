package baseline;

import java.util.ArrayList;

import util.FileUtils;

public class Baseline {
    
    private String[] badWords;
    private String frase;

    public Baseline(String frase){
        this.frase = frase;
        this.badWords = FileUtils.readBadWords();
    }

    public boolean isBadWord(String palavra){
        for(int i = 0; i < this.badWords.length; i++){
            if(this.badWords[i].equals(palavra))
                return true;
        }
        return false;
    }

    public ArrayList<Integer> hasBadWord() {
        ArrayList<Integer> posicaoBadWords = new ArrayList<>();
        String[] entrada = this.frase.split(" ");

        for(int i = 0; i < entrada.length; i++){
            if(isBadWord(entrada[i])){
                posicaoBadWords.add(i);
            }
        }

        if(posicaoBadWords.isEmpty()) return null;
        return posicaoBadWords;
    }
}