package aho_corasick;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class AhoCorasick {
    private final Node root;
    private final Map<Character, ArrayList<Character>> leetMap;

    private static class Node {
        Node[] next = new Node[26];
        Node fail;
        Node dictLink;
        String word; 
    }

    public AhoCorasick(String[] words, Map<Character, ArrayList<Character>> leetMap) {
        this.root = new Node();
        this.leetMap = leetMap;
        for (String s : words) {
            insert(s);
        }
        build();
    }

    private void insert(String s) {
        Node curr = root;
        for (char c : s.toCharArray()) {
            int idx = c - 'a';
            if (idx < 0 || idx >= 26) continue;
            if (curr.next[idx] == null) curr.next[idx] = new Node();
            curr = curr.next[idx];
        }
        curr.word = s;
    }

    private void build() {
        Queue<Node> q = new ArrayDeque<>();
        for (int i = 0; i < 26; i++) {
            if (root.next[i] != null) {
                root.next[i].fail = root;
                q.add(root.next[i]);
            } else {
                root.next[i] = root;
            }
        }

        while (!q.isEmpty()) {
            Node curr = q.poll();
            for (int i = 0; i < 26; i++) {
                if (curr.next[i] != null) {
                    Node child = curr.next[i];
                    child.fail = curr.fail.next[i];
                    child.dictLink = (child.fail.word != null) ? child.fail : child.fail.dictLink;
                    q.add(child);
                } else {
                    curr.next[i] = curr.fail.next[i];
                }
            }
        }
    }

    public int countBadWords(String text) {
        if (text == null) return 0;
        
        int count = 0;
        List<String> variations = generateCleanTexts(text);

        for (String variant : variations) {
            Node curr = root;
            for (char c : variant.toCharArray()) {
                int idx = c - 'a';
                if (idx < 0 || idx >= 26) continue;

                curr = curr.next[idx];
                Node temp = (curr.word != null) ? curr : curr.dictLink;
                while (temp != null) {
                    count++;
                    temp = temp.dictLink;
                }
            }
        }
        return count;
    }

    public List<String> generateCleanTexts(String text) {
        List<String> results = new ArrayList<>();
        results.add(""); 

        for (char c : text.toLowerCase().toCharArray()) {
            char target = c;
            List<Character> options = new ArrayList<>();

            if (leetMap.containsKey(c)) {
                options.addAll(leetMap.get(c));
            } else if (c >= 'a' && c <= 'z') {
                options.add(c);
            } else {
                continue;
            }

            List<String> nextResults = new ArrayList<>();
            for (String s : results) {
                for (char opt : options) {
                    nextResults.add(s + opt);
                }
            }
            results = nextResults;
        }
        return results;
    }

    public boolean isBadWord(String word) {
        if (word == null || word.isEmpty()) return false;

        Node curr = root;
        for (char c : word.toLowerCase().toCharArray()) {
            int idx = c - 'a';
            
            if (idx < 0 || idx >= 26 || curr.next[idx] == null || curr.next[idx] == root) {
                return false;
            }
            
            curr = curr.next[idx];
        }

        return curr.word != null;
    }
}