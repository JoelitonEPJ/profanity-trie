package trie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.Pair;

public class Trie {

    private final Node root;
    private final Map<Character, ArrayList<Character>> leetMap;

    public Trie(Map<Character, ArrayList<Character>> leetMap) {
        this.root = new Node();
        this.leetMap = leetMap;
    }

    public Trie(String[] words, Map<Character, ArrayList<Character>> leetMap) {
        this.root = new Node();
        this.leetMap = leetMap;
        for (String word : words) {
            addWord(word, -1);
        }
    }

    public Trie(Pair<String, Integer>[] wordsFirstPrefix, Map<Character, ArrayList<Character>> leetMap) {
        this.root = new Node();
        this.leetMap = leetMap;
        for (Pair<String, Integer> word : wordsFirstPrefix) {
            addWord(word.first(), word.second());
        }
    }

    public void addWord(String text, int index) {
        Node current = this.root;
        int nodeCounting = 0;

        for (char caracter : text.toCharArray()) {
            if (caracter == '-') continue;

            current.childs.putIfAbsent(caracter, new Node());
            current = current.childs.get(caracter);

            if (nodeCounting == index) current.isBadNode = true;

            nodeCounting++;
        }

        current.end = true;
    }

    private int findLongestBadWordIndex(String phrase, int j, Node current) {
        int size = phrase.length();

        if (current == null || j >= size) return -1;

        List<Character> possibilities = getCharFromLeet(phrase.charAt(j));
        int maxMatch = -1;

        if (possibilities == null) {
            if (current == this.root) return -1;
            return findLongestBadWordIndex(phrase, j + 1, current);
        }

        for (char nextChar : possibilities) {
            Node next = current.childs.get(nextChar);

            if (next != null) {
                if (next.isBadNode || next.end && (j == size - 1 || Character.isWhitespace(phrase.charAt(j + 1)))) {
                    maxMatch = Math.max(maxMatch, j);
                }

                int currentMatch = findLongestBadWordIndex(phrase, j + 1, next);

                maxMatch = Math.max(maxMatch, currentMatch);
            }
        }
        return maxMatch;
    }

    public int countBadWords(String phrase) {
        int count = 0;
        int size = phrase.length();

        for (int i = 0; i < size;) {
            int lastMatchIndex = findLongestBadWordIndex(phrase, i, this.root);

            if (lastMatchIndex != -1) {
                i = nextIndexAfterWhiteSpace(phrase, lastMatchIndex);
                count++;
            } else {
                i = nextIndexAfterWhiteSpace(phrase, i);
            }
        }

        return count;
    }

    public boolean checkIsBadWord(String word) {
        return checkIsBadWord(word, 0, this.root);
    }

    private boolean checkIsBadWord(String word, int index, Node nodeAtual) {

        if (nodeAtual == null) return false;
        if (nodeAtual.isBadNode) return true;
        if (index == word.length()) return nodeAtual.end;

        List<Character> possibilidades = getCharFromLeet(word.charAt(index));
        if (possibilidades == null) {
            return checkIsBadWord(word, index + 1, nodeAtual);
        }

        for (char letraASeguir : possibilidades) {
            Node proximoNo = nodeAtual.childs.get(letraASeguir);

            if (checkIsBadWord(word, index + 1, proximoNo)) {
                return true;
            }
        }

        return false;
    }

    private List<Character> getCharFromLeet(char leet) {
        List<Character> possibilities = leetMap.get(leet);
        return possibilities == null ? leetMap.get(Character.toLowerCase(leet)) : possibilities;
    }

    private int nextIndexAfterWhiteSpace(String phrase, int index) {
        int i = index - 1;

        while (++i < phrase.length() && !Character.isWhitespace(phrase.charAt(i))) {}
        while (++i < phrase.length() && Character.isWhitespace(phrase.charAt(i))) {}
        return i;
    }

    private static class Node {

        boolean end;
        boolean isBadNode;
        HashMap<Character, Node> childs;

        public Node() {
            this.end = false;
            this.isBadNode = false;
            this.childs = new HashMap<>();
        }
    }
}
