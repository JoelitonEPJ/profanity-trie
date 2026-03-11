package trie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import util.Pair;

public class Trie {

    private final Node root;
    private final Map<Character, ArrayList<Character>> leetMap;

    public Trie(Map<Character, ArrayList<Character>> leetMap){
        this.root = new Node();
        this.leetMap = leetMap;
    }

    public Trie(String[] words, Map<Character, ArrayList<Character>> leetMap){
        this.root = new Node();
        this.leetMap = leetMap;
        for (int i = 0; i < words.length; i++) {
            addWord(words[i], -1);
        }
    }

    public Trie(Pair<String, Integer>[] wordsFirstPrefix, Map<Character, ArrayList<Character>> leetMap){
        this.root = new Node();
        this.leetMap = leetMap;
        for (Pair<String, Integer> word : wordsFirstPrefix) {
            addWord(word.first(), word.second());
        }
    }

    public void addWord(String text, int index){
        Node current = this.root;
        int nodeCounting = 0;
    
        for (char caracter : text.toCharArray()){
            if (caracter == '-') {
                continue;
            }

            current.childs.putIfAbsent(caracter, new Node());
            current = current.childs.get(caracter);

            nodeCounting++;

            if (nodeCounting == index) {
                current.isBadNode = true;
            }
        }

        current.end = true;
    }
    
    public int countBadWords(String phrase){
        int count = 0;
        int size = phrase.length();

        for (int i = 0; i < size; i++) {
            Node current = this.root;
            int lastMatchIndex = -1;

            for (int j = i; j < size; j++) {
                char caracter = phrase.charAt(j);
                
                if (caracter == '-') {
                    continue;
                }

                current = current.childs.get(caracter);

                if (current == null) {
                    break;
                }

                if (current.isBadNode || current.end) {
                    lastMatchIndex = j;
                }
            }

            if (lastMatchIndex != -1) {
                count++;
                i = lastMatchIndex;
            }
        }
        
        return count;
    }

    public boolean checkIsBadWord(String word) {
        return checkIsBadWord(word, 0, this.root);
    }

    private boolean checkIsBadWord(String word, int index, Node nodeAtual) {

        if (nodeAtual == null) return false;

        if (nodeAtual.isBadNode) return true;

        if (index == word.length()) return nodeAtual.end; 

        char caracter = Character.toLowerCase(word.charAt(index));

        if (leetMap.get(caracter) == null) {
            return checkIsBadWord(word, index + 1, nodeAtual);
        }

        ArrayList<Character> possibilidades = leetMap.get(caracter);
        
        for (char letraASeguir : possibilidades) {
            Node proximoNo = nodeAtual.childs.get(letraASeguir);
    
            if (checkIsBadWord(word, index + 1, proximoNo)) {
                return true;
            }
        }
        
        return false;
    }


    private static class Node {

        boolean end;
        boolean isBadNode;
        HashMap<Character,Node> childs;

        public Node(){
            this.end = false;
            this.isBadNode = false;
            this.childs = new HashMap<>();
        }
    }    
}