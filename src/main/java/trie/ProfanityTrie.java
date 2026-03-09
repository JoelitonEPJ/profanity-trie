package trie;

import java.util.HashMap;

public class ProfanityTrie {

    private final Node root;

    public ProfanityTrie(){
        this.root = new Node();
    }

    public void addWords(String text, int index){
            if (index == -1) {
                index = text.length() - 1; 
            }

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
            return false;
        }
    private static class Node {
        boolean end;
        boolean isBadNode;
        int counting;
        HashMap<Character,Node> childs = new HashMap<>();

        public Node(){
            this.end = false;
            this.counting = 0;
            this.childs = new HashMap<>();
            this.isBadNode = false;
        }
    }    
}