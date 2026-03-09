package trie;

import java.util.HashMap;
import java.util.Map;

public class Trie {
    public static class Node {
        
<<<<<<< HEAD
        boolean fim;
        int contagem;    
        Map<Character,Node> filhos;    

=======
        boolean end;
        int counting;    
        Map<Character,Node> childs;    
        
>>>>>>> origin/main
        public Node(){
            this.end = false;
            this.counting = 0;
            this.childs = new HashMap<>();
        }
    }
        
    private final Node root;
        
<<<<<<< HEAD
    public Trie(){
        this.raiz = new Node();
    }

    public void insert(String texto){
        Node atual = raiz;

        for (char caracter : texto.toCharArray()){
            atual.filhos.putIfAbsent(caracter, new Node());

            atual = atual.filhos.get(caracter);
            atual.contagem++;
        }
        atual.fim = true;
    }

    public int query(String alvo){
        Node atual = raiz;

        for (char caracter : alvo.toCharArray()){
            atual = atual.filhos.get(caracter);

            if (atual == null) return 0;
        }

        return atual.contagem;
    }
}
=======
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
>>>>>>> origin/main
