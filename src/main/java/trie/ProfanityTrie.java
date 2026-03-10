package trie;

import java.util.HashMap;

public class ProfanityTrie {

    private final Node root;

    public ProfanityTrie(){
        this.root = new Node();
    }

    public ProfanityTrie(String[] words){
        this.root = new Node();
        for (int i = 0; i < words.length; i++) addWords(words[i], -1);
    }

    public void addWords(String text, int index){
            Node current = root;
            int nodeCounting = 0;
    
            for (char caracter : text.toCharArray()){
                current.childs.putIfAbsent(caracter, new Node());
                current = current.childs.get(caracter);

                current.counting++;
                nodeCounting++;

                if (nodeCounting == index){
                    current.isBadNode = true;
                }
            }

            current.end = true;
        }
    
    public int countBadWords(String phrase){
            int count = 0;
            String[] words = phrase.split(" ");
            
            for (String x : words){
                if (checkIsBadWord(x)) count++;
            }

            return count;
    }
    

    public boolean checkIsBadWord(String alvo){
        Node current = root;
        
        for (char caracter : alvo.toCharArray()){
            current = current.childs.get(caracter);

            if (current == null) return false;

            if (current.isBadNode) return true;
        }

        return current.end;
    }
    private static class Node {
        boolean end;
        boolean isBadNode;
        int counting;
        HashMap<Character,Node> childs;

        public Node(){
            this.end = false;
            this.counting = 0;
            this.childs = new HashMap<>();
            this.isBadNode = false;
        }
    }    
}