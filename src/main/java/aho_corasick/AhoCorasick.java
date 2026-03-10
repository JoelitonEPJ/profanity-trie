package aho_corasick;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public final class AhoCorasick {
    private final Node root;
    private final String[] patterns;
    private final Map<Character, ArrayList<Character>> leetMap;

    public AhoCorasick(String[] words, Map<Character, ArrayList<Character>> leetMap) {
        this.root = new Node();
        this.patterns = words;
        this.leetMap = leetMap;

        buildTrie();
        buildSuffixAndOutputLinks();
    }

    
    private void buildTrie() {
        for (int i = 0; i < patterns.length; i++) {
            Node curr = root;
            
            for (int j = 0; j < patterns[i].length(); j++) {
                char c = patterns[i].charAt(j);
                
                if (!curr.child.containsKey(c)) {
                    curr.child.put(c, new Node());
                }

                curr = curr.child.get(c);
            }
            curr.patternInd = i;
        }
    }
    
    private void buildSuffixAndOutputLinks() {
        root.suffixLink = root;
        Queue<Node> q = new LinkedList<>();
        
        for (char rc : root.child.keySet()) {
            Node childNode = root.child.get(rc);
            q.add(childNode);
            childNode.suffixLink = root;
        }
        
        while (!q.isEmpty()) {
            Node currentState = q.poll();
            
            for (char cc : currentState.child.keySet()) {
                Node currentChild = currentState.child.get(cc);
                Node parentSuffix = currentState.suffixLink;
                
                while (!parentSuffix.child.containsKey(cc) && parentSuffix != root) {
                    parentSuffix = parentSuffix.suffixLink;
                }
                
                if (parentSuffix.child.containsKey(cc)) {
                    currentChild.suffixLink = parentSuffix.child.get(cc);
                } else {
                    currentChild.suffixLink = root;
                }

                
                if (currentChild.suffixLink.patternInd >= 0) {
                    currentChild.outputLink = currentChild.suffixLink;
                } else {
                    currentChild.outputLink = currentChild.suffixLink.outputLink;
                }
                
                q.add(currentChild);
            }
        }
    }
    
    
    public List<List<Integer>> searchIn(final String text) {
        List<List<Integer>> positionByStringIndexValue = initializePositionByStringIndexValue();
        Node curr = root;
        
        PatternPositionRecorder positionRecorder = new PatternPositionRecorder(positionByStringIndexValue);
        
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            
            while (curr != root && !curr.child.containsKey(ch)) {
                curr = curr.suffixLink;
            }
            
            if (curr.child.containsKey(ch)) {
                curr = curr.child.get(ch);
                positionRecorder.recordPatternPositions(curr, i);
            }
        }
        
        setUpStartPoints(positionByStringIndexValue);
        return positionByStringIndexValue;
    }
    
    private List<List<Integer>> initializePositionByStringIndexValue() {
        List<List<Integer>> positionByStringIndexValue = new ArrayList<>(patterns.length);
        for (int i = 0; i < patterns.length; i++) {
            positionByStringIndexValue.add(new ArrayList<>());
        }
        return positionByStringIndexValue;
    }
    
    private void setUpStartPoints(List<List<Integer>> positionByStringIndexValue) {
        for (int i = 0; i < patterns.length; i++) {
            List<Integer> positions = positionByStringIndexValue.get(i);
            int patternLength = patterns[i].length();
            for (int j = 0; j < positions.size(); j++) {
                int endpoint = positions.get(j);
                positions.set(j, endpoint - patternLength + 1);
            }
        }
    }
    
    private static class PatternPositionRecorder {
        
        private final List<List<Integer>> positionByStringIndexValue;
        
        PatternPositionRecorder(List<List<Integer>> positionByStringIndexValue) {
            this.positionByStringIndexValue = positionByStringIndexValue;
        }
        
        public void recordPatternPositions(final Node parent, final int currentPosition) {
            if (parent.patternInd > -1) {
                positionByStringIndexValue.get(parent.patternInd).add(currentPosition);
            }
            
            Node outputLink = parent.outputLink;
            while (outputLink != null) {
                positionByStringIndexValue.get(outputLink.patternInd).add(currentPosition);
                outputLink = outputLink.outputLink;
            }
        }
    }
    
    private static class Node {
        final Map<Character, Node> child = new HashMap<>();
        Node suffixLink;
        Node outputLink;
        int patternInd;
    
        Node() {
            this.suffixLink = null;
            this.outputLink = null;
            this.patternInd = -1;
        }
    }

    public static void main(String[] args) {
        // 1. Definir os padrões que queremos encontrar
        String[] patterns = {"he", "she", "hers", "his"};
        
        // 2. Criar o mapa de Leet Speak (vazio por enquanto, já que a lógica ainda é literal)
        Map<Character, ArrayList<Character>> leetMap = new HashMap<>();

        // 3. Inicializar a Aho-Corasick
        System.out.println("--- Inicializando Aho-Corasick ---");
        AhoCorasick aho = new AhoCorasick(patterns, leetMap);

        // 4. Texto de teste
        // "ushers" contém "she" e "he"
        // "his" contém "his"
        String text = "ushershis";
        System.out.println("Buscando em: \"" + text + "\"");

        // 5. Executar a busca
        List<List<Integer>> results = aho.searchIn(text);

        // 6. Exibir os resultados de forma legível
        System.out.println("\n--- Resultados Encontrados ---");
        for (int i = 0; i < patterns.length; i++) {
            String word = patterns[i];
            List<Integer> positions = results.get(i);
            
            if (positions.isEmpty()) {
                System.out.println("Palavra '" + word + "': Nenhuma ocorrência.");
            } else {
                System.out.println("Palavra '" + word + "': Encontrada nas posições " + positions);
                
                // Validação visual
                for (int pos : positions) {
                    String substring = text.substring(pos, pos + word.length());
                    System.out.println("   -> Confirmado em [" + pos + "]: " + substring);
                }
            }
        }
    }
}