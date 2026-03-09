package aho_corasick;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class AhoCorasick {

    static class Node {
        Node[] next = new Node[26];
        Node fail;
        List<String> out = new ArrayList<>();
    }

    static Node root = new Node();

    static void insert(String s) {
        Node curr = root;

        for (char c : s.toCharArray()) {
            int idx = c - 'a';

            if (curr.next[idx] == null)
                curr.next[idx] = new Node();

            curr = curr.next[idx];
        }

        curr.out.add(s);
    }

    static void build() {
        Queue<Node> q = new ArrayDeque<>();

        for (int i = 0; i < 26; i++) {
            if (root.next[i] != null) {
                root.next[i].fail = root;
                q.add(root.next[i]);
            }
        }

        while (!q.isEmpty()) {
            Node curr = q.poll();

            for (int i = 0; i < 26; i++) {
                if (curr.next[i] != null) {
                    Node f = curr.fail;

                    while (f != null && f.next[i] == null)
                        f = f.fail;

                    curr.next[i].fail = (f == null) ? root : f.next[i];

                    curr.next[i].out.addAll(curr.next[i].fail.out);

                    q.add(curr.next[i]);
                }
            }
        }
    }

    static void search(String text) {
        Node curr = root;

        for (int i = 0; i < text.length(); i++) {
            int idx = text.charAt(i) - 'a';

            while (curr != root && curr.next[idx] == null)
                curr = curr.fail;

            if (curr.next[idx] != null)
                curr = curr.next[idx];

            for (String s : curr.out)
                System.out.println("\"" + s + "\" no índice: " + (i - s.length() + 1));
        }
    }

    public static void main(String[] args) {}
}