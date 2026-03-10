package aho_corasick;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

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
            String pattern = patterns[i];
            
            for (char c : pattern.toCharArray()) {
                c = Character.toLowerCase(c);

                if (!isValidChar(c)) continue;
                
                curr = curr.child.computeIfAbsent(c, k -> new Node());
            }
            
            curr.addPattern(i);
        }
    }
    
    private void buildSuffixAndOutputLinks() {
        root.suffixLink = root;
        Queue<Node> queue = new LinkedList<>();
        
        for (Node child : root.child.values()) {
            child.suffixLink = root;
            queue.add(child);
        }
        
        while (!queue.isEmpty()) {
            Node current = queue.poll();
            
            for (Map.Entry<Character, Node> entry : current.child.entrySet()) {
                char c = entry.getKey();
                Node child = entry.getValue();
                
                Node suffix = current.suffixLink;
                while (suffix != root && !suffix.child.containsKey(c)) {
                    suffix = suffix.suffixLink;
                }
                
                if (suffix.child.containsKey(c)) {
                    child.suffixLink = suffix.child.get(c);
                } else {
                    child.suffixLink = root;
                }
                
                if (child.suffixLink.hasPatterns()) {
                    child.outputLink = child.suffixLink;
                } else {
                    child.outputLink = child.suffixLink.outputLink;
                }
                
                queue.add(child);
            }
        }
    }
    
    public boolean isBadWord(String text) {
        if (text == null) return false;
        
        Node currState = root;
        
        for (char c : text.toCharArray()) {
            c = Character.toLowerCase(c);

            if (Character.isWhitespace(c)) {
                currState = root;
                continue;
            }
            
            if (!isValidChar(c)) continue;
            
            List<Character> interpretations = getInterpretations(c);
            
            for (char opt : interpretations) {
                Node next = transition(currState, opt);
                if (next != null) {
                    currState = next;
                    if (hasAnyMatch(currState)) return true;
                    break;
                }
            }
        }
        
        return false;
    }
    
    public int countBadWords(String text) {
        if (text == null) return 0;
        
        Node currState = root;
        int matches = 0;
        Set<Integer> matchedPatterns = new HashSet<>();
        
        for (char c : text.toCharArray()) {
            c = Character.toLowerCase(c);

            if (Character.isWhitespace(c)) {
                currState = root;
                matchedPatterns.clear();
                continue;
            }
            
            if (!isValidChar(c)) continue;
            
            List<Character> interpretations = getInterpretations(c);
            
            for (char opt : interpretations) {
                Node next = transition(currState, opt);
                if (next != null) {
                    currState = next;
                    
                    Set<Integer> newMatches = getUniqueMatches(currState, matchedPatterns);
                    matches += newMatches.size();
                    matchedPatterns.addAll(newMatches);
                    
                    break;
                }
            }
        }
        
        return matches;
    }
    
    private Node transition(Node state, char c) {
        Node curr = state;
        while (curr != root && !curr.child.containsKey(c)) {
            curr = curr.suffixLink;
        }
        return curr.child.getOrDefault(c, null);
    }
    
    private boolean hasAnyMatch(Node node) {
        Node curr = node;
        while (curr != null) {
            if (curr.hasPatterns()) return true;
            curr = curr.outputLink;
        }
        return false;
    }
    
    private Set<Integer> getUniqueMatches(Node node, Set<Integer> alreadyMatched) {
        Set<Integer> matches = new HashSet<>();
        Node curr = node;
        
        while (curr != null) {
            for (int patternIndex : curr.patternIndices) {
                if (!alreadyMatched.contains(patternIndex)) {
                    matches.add(patternIndex);
                }
            }
            curr = curr.outputLink;
        }
        
        return matches;
    }
    
    private boolean isValidChar(char c) {
        return (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || leetMap.containsKey(c);
    }
    
    private List<Character> getInterpretations(char c) {
        List<Character> interpretations = new ArrayList<>();
        interpretations.add(c);
        
        List<Character> leetOptions = leetMap.get(c);
        if (leetOptions != null) {

            for (char opt : leetOptions) {
                if (opt != c) {
                    interpretations.add(opt);
                }
            }
        }
        
        return interpretations;
    }
    
    private static class Node {
        final Map<Character, Node> child = new HashMap<>();
        Node suffixLink;
        Node outputLink;
        Set<Integer> patternIndices = new HashSet<>();
        
        void addPattern(int index) {
            patternIndices.add(index);
        }
        
        boolean hasPatterns() {
            return !patternIndices.isEmpty();
        }
    }

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║     TESTES DO AHOCORASICK             ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        testarCasoBasico();
        testarLeetSpeak();
        testarEspacosESeparadores();
        testarMultiplosMatches();
        testarCasosLimite();
        testarCenarioReal();
    }
    
    private static void testarCasoBasico() {
        System.out.println("📌 TESTE 1 - CASO BÁSICO");
        System.out.println("──────────────────────────────");
        
        AhoCorasick aho = new AhoCorasick(
            new String[]{"she", "he", "his", "hers"},
            new HashMap<>()
        );
        
        testar(aho, "she", 2, "she → she + he");
        testar(aho, "he", 1, "he → he");
        testar(aho, "his", 1, "his → his");
        testar(aho, "hers", 2, "hers → hers + he");
        System.out.println();
    }
    
    private static void testarLeetSpeak() {
        System.out.println("📌 TESTE 2 - LEET SPEAK");
        System.out.println("──────────────────────────────");
        
        Map<Character, ArrayList<Character>> leet = new HashMap<>();
        leet.put('4', new ArrayList<>(Arrays.asList('a')));
        leet.put('@', new ArrayList<>(Arrays.asList('a')));
        leet.put('0', new ArrayList<>(Arrays.asList('o')));
        leet.put('3', new ArrayList<>(Arrays.asList('e')));
        leet.put('5', new ArrayList<>(Arrays.asList('s')));
        leet.put('$', new ArrayList<>(Arrays.asList('s')));
        
        AhoCorasick aho = new AhoCorasick(
            new String[]{"casa", "bola", "teste", "she", "he"},
            leet
        );
        
        testar(aho, "c4s4", 1, "c4s4 → casa");
        testar(aho, "c@$@", 1, "c@$@ → casa");
        testar(aho, "b0l@", 1, "b0l@ → bola");
        testar(aho, "t3st3", 1, "t3st3 → teste");
        testar(aho, "sh3", 1, "sh3 → she (deve contar apenas 1)");
        testar(aho, "h3", 1, "h3 → he");
        System.out.println();
    }
    
    private static void testarEspacosESeparadores() {
        System.out.println("📌 TESTE 3 - ESPAÇOS E SEPARADORES");
        System.out.println("──────────────────────────────");
        
        AhoCorasick aho = new AhoCorasick(
            new String[]{"casa", "bola", "teste"},
            new HashMap<>()
        );
        
        testar(aho, "casa bola", 2, "espaço simples");
        testar(aho, "casa  bola", 2, "múltiplos espaços");
        testar(aho, "casa.bola", 2, "ponto como separador? (não separa)");
        testar(aho, "casa bola teste", 3, "três palavras");
        testar(aho, "casa", 1, "palavra única");
        System.out.println();
    }
    
    private static void testarMultiplosMatches() {
        System.out.println("📌 TESTE 4 - MÚLTIPLOS MATCHES");
        System.out.println("──────────────────────────────");
        
        AhoCorasick aho = new AhoCorasick(
            new String[]{"a", "ab", "abc", "abcd"},
            new HashMap<>()
        );
        
        testar(aho, "abcd", 4, "abcd → a, ab, abc, abcd");
        
        AhoCorasick aho2 = new AhoCorasick(
            new String[]{"he", "she", "his", "hers"},
            new HashMap<>()
        );
        
        testar(aho2, "hers", 2, "hers → hers, he");
        testar(aho2, "she", 2, "she → she, he");
        testar(aho2, "she is here", 3, "she(2) + here(1) = 3");
        System.out.println();
    }
    
    private static void testarCasosLimite() {
        System.out.println("📌 TESTE 5 - CASOS LIMITE");
        System.out.println("──────────────────────────────");
        
        AhoCorasick aho = new AhoCorasick(
            new String[]{"test", "abc"},
            new HashMap<>()
        );
        
        testar(aho, null, 0, "texto null");
        testar(aho, "", 0, "texto vazio");
        testar(aho, "   ", 0, "apenas espaços");
        testar(aho, "!@#$%", 0, "apenas caracteres especiais");
        testar(aho, "TEST", 1, "case insensitive");
        
        // Teste com padrões vazios ou null
        AhoCorasick aho2 = new AhoCorasick(
            new String[]{"  ", "valid"},
            new HashMap<>()
        );
        testar(aho2, "valid", 1, "ignora padrões inválidos");
        System.out.println();
    }
    
    private static void testarCenarioReal() {
        System.out.println("📌 TESTE 6 - CENÁRIO REAL (Filtro de Profanidade)");
        System.out.println("──────────────────────────────");
        
        Map<Character, ArrayList<Character>> leetProfanity = new HashMap<>();
        leetProfanity.put('4', new ArrayList<>(Arrays.asList('a')));
        leetProfanity.put('@', new ArrayList<>(Arrays.asList('a')));
        leetProfanity.put('0', new ArrayList<>(Arrays.asList('o')));
        leetProfanity.put('3', new ArrayList<>(Arrays.asList('e')));
        leetProfanity.put('1', new ArrayList<>(Arrays.asList('i')));
        leetProfanity.put('5', new ArrayList<>(Arrays.asList('s')));
        leetProfanity.put('$', new ArrayList<>(Arrays.asList('s')));
        
        String[] badWords = {
            "idiota", "burro", "estupido", "palavrao", "proibido"
        };
        
        AhoCorasick filtro = new AhoCorasick(badWords, leetProfanity);
        
        System.out.println("📝 Frases ofensivas:");
        testar(filtro, "você é um idiota", 1, "palavra explícita");
        testar(filtro, "seu burro", 1, "palavra explícita");
        testar(filtro, "que 1d10t4", 1, "leet speak - idiota");
        testar(filtro, "vc é um 3stup1d0", 1, "leet speak - estupido");
        testar(filtro, "palavr4o proib1do", 2, "múltiplas palavras em leet");
        
        System.out.println("\n📝 Frases seguras:");
        testar(filtro, "bom dia", 0, "sem ofensas");
        testar(filtro, "como você está?", 0, "sem ofensas");
        testar(filtro, "123456", 0, "números sem sentido");
    }
    
    private static void testar(AhoCorasick aho, String texto, int esperado, String descricao) {
        int resultado = texto != null ? aho.countBadWords(texto) : 0;
        boolean isBad = texto != null && aho.isBadWord(texto);
        boolean isBadEsperado = esperado > 0;
        
        String status = (resultado == esperado && isBad == isBadEsperado) ? "✅" : "❌";
        String isBadStr = isBad ? "BAD" : "OK ";
        
        System.out.printf("%s '%s' → %d matches | %s | (esperado: %d) - %s\n", 
            status, texto, resultado, isBadStr, esperado, descricao);
        
        if (resultado != esperado) {
            System.out.printf("  ⚠️  FALHOU: esperado %d, obtido %d\n", esperado, resultado);
        }
    }
}