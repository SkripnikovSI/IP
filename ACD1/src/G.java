import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class G {
    private static final int SZ = 26;
    private static ArrayList<Node> states = new ArrayList<Node>();

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("search4.in"));
        PrintWriter out = new PrintWriter("search4.out");
        int n = Integer.parseInt(in.readLine());
        createTrie(in, n);
        boolean[] answer = new boolean[n];
        boolean[] dead = new boolean[states.size()];
        String t = in.readLine();
        int state = 0;
        for (int i = 0; i < t.length(); i++) {
            int c = t.charAt(i) - 'a';
            state = states.get(state).go(c);
            int tmpState = state;
            while (tmpState != 0 && !dead[tmpState]) {
                dead[tmpState] = true;
                if (states.get(tmpState).patterns != null)
                    for (int p : states.get(tmpState).patterns)
                        answer[p] = true;
                tmpState = states.get(tmpState).getSuffLink();
            }
        }
        for (boolean b : answer)
            if (b)
                out.println("YES");
            else
                out.println("NO");
        out.close();
    }


    private static void createTrie(BufferedReader in, int n) throws IOException {
        states.add(new Node(0, -1));
        for (int i = 0; i < n; i++) {
            String str = in.readLine();
            int lastState = 0;
            for (int j = 0; j < str.length(); j++) {
                int c = str.charAt(j) - 'a';
                if (states.get(lastState).go[c] == -1) {
                    states.get(lastState).go[c] = states.size();
                    states.add(new Node(lastState, c));
                }
                lastState = states.get(lastState).go[c];
            }
            if (states.get(lastState).patterns == null) {
                states.get(lastState).patterns = new ArrayList<Integer>();
            }
            states.get(lastState).patterns.add(i);
        }
    }


    private static class Node {
        private int parent;
        private int c;
        private ArrayList<Integer> patterns;

        private int[] go = new int[SZ];
        private int suffLink = -1;

        Node(int parent, int c) {
            for (int i = 0; i < SZ; i++)
                go[i] = -1;
            this.parent = parent;
            this.c = c;
        }

        private int getSuffLink() {
            if (suffLink == -1)
                if (c == -1 || parent == 0)
                    suffLink = 0;
                else
                    suffLink = states.get(states.get(parent).getSuffLink()).go(c);
            return suffLink;
        }

        private int go(int c) {
            if (go[c] == -1)
                go[c] = this.c == -1 ? 0 : states.get(getSuffLink()).go(c);
            return go[c];
        }
    }
}
