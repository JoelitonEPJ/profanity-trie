package trie;

import java.util.HashMap;

public class Trie {

    private final Node root;

    public Trie (){
        this.root = new Node();
    }

    public Trie(String[] words){
        this.root = new Node();
        for (int i = 0; i < words.length; i++) addWords(words[i], -1);
    }

    public void addWords(String text, int index){
        Node current = root;
        int nodeCounting = 0;
    
        for (char caracter : text.toCharArray()){
            if (caracter == '-') {
                 continue;
            }

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
        int size = phrase.length();

        for (int i = 0; i < size; i++){
            Node current = root;
            int lastMatchIndex = -1;

            for (int j = i; j < size; j++){
                char caracter = phrase.charAt(j);
                
                if (caracter == '-'){
                    continue;
                }

                current = current.childs.get(caracter);

                if (current == null){
                    break;
                }

                if (current.isBadNode || current.end){
                    lastMatchIndex = j;
                }
            }

            if (lastMatchIndex != -1){
                count++;
                i = lastMatchIndex;
            }
        }
        return count;
    }
    

    public boolean checkIsBadWord(String alvo){
        Node current = root;
        
        for (char caracter : alvo.toCharArray()){

            if (caracter == '-'){
                continue;
            }

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