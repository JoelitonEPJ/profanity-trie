package trie;

import java.util.HashMap;
import java.util.Map;

public class Trie {
    private static class Node {
        
        boolean end;
        int counting;    
        Map<Character,Node> childs;    
        
        public Node(){
            this.end = false;
            this.counting = 0;
            this.childs = new HashMap<>();
        }
    }
        
    private final Node root;
        
    public Trie(){
        this.root = new Node();
    }

    public void insert(String texto){
        Node current = root;

        for (char caracter : texto.toCharArray()){
            current.childs.putIfAbsent(caracter, new Node());

            current = current.childs.get(caracter);
            current.counting++;
        }
        current.end = true;
    }

    public int query(String alvo){
        Node current = root;

        for (char caracter : alvo.toCharArray()){
            current = current.childs.get(caracter);

            if (current == null) return 0;
        }

        return current.counting;
    }
}