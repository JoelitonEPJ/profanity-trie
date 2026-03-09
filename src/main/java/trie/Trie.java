package trie;

import java.util.HashMap;
import java.util.Map;

public class Trie {
    public static class Node {
        
        boolean fim;
        int contagem;    
        Map<Character,Node> filhos;    

        public Node(){
            this.fim = false;
            this.contagem = 0;
            this.filhos = new HashMap<>();
        }
    }
        
    private final Node raiz;
        
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