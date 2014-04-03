import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class D {
    private static ArrayList<Integer>[] m;
    private static int N;
    private static int M;
    private static int S;
    private static int[] parity;

    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(new FileReader("game.in"));
        PrintWriter out = new PrintWriter("game.out");
        N = in.nextInt();
        M = in.nextInt();
        S = in.nextInt();
        parity = new int[N + 1];
        m = new ArrayList[N + 1];
        for (int i = 0; i < M; i++) {
            int v = in.nextInt();
            int u = in.nextInt();
            if (m[v] == null)
                m[v] = new ArrayList<Integer>();
            m[v].add(u);
        }
        if (dFS(S, true)) {
            out.print("First player wins");
        } else {
            out.print("Second player wins");
        }
        out.close();
    }

    private static boolean dFS(int v, boolean fWin) {
        if (parity[v] == 1) {
            return fWin;
        } else if (parity[v] == 2) {
            return !fWin;
        }
        if (m[v] == null) {
            parity[v] = 2;
            return !fWin;
        } else {
            for (int i = 0; i < m[v].size(); i++) {
                int u = m[v].get(i);
                if (fWin == dFS(u, !fWin)) {
                    parity[v] = 1;
                    return fWin;
                }
            }
            parity[v] = 2;
            return !fWin;
        }
    }
}
