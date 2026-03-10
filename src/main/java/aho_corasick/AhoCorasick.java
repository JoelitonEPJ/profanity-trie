package aho_corasick;

import java.util.ArrayList;
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
            Node curr = queue.poll();
            
            for (Map.Entry<Character, Node> entry : curr.child.entrySet()) {
                char c = entry.getKey();
                Node child = entry.getValue();
                
                Node suffix = curr.suffixLink;
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
    
    public boolean isBadWord(String word) {
        if (word == null) return false;
        
        Node currState = root;
        
        for (char c : word.toCharArray()) {
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
    
    private Node transition(Node n, char c) {
        Node curr = n;
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
    
    private boolean isValidChar(char c) {
        return (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || leetMap.containsKey(c);
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
}