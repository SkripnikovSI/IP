import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Stack;

public class C {

    private static ArrayList<Integer>[] G;
    private static ArrayList<Integer>[] GT;
    private static ArrayList<Integer>[] W;
    private static Stack<Integer> o = new Stack<Integer>();
    private static boolean[] isUsed;
    private static int[] uvw;
    private static int N;
    private static int M;
    private static int s;
    private static int t;

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("shortpath.in"));
        PrintWriter out = new PrintWriter("shortpath.out");
        String parts[] = in.readLine().split(" ");
        N = Integer.parseInt(parts[0]);
        M = Integer.parseInt(parts[1]);
        s = Integer.parseInt(parts[2]);
        t = Integer.parseInt(parts[3]);
        G = new ArrayList[N + 1];
        GT = new ArrayList[N + 1];
        W = new ArrayList[N + 1];
        uvw = new int[N + 1];
        isUsed = new boolean[N + 1];
        for (int i = 0; i < M; i++) {
            parts = in.readLine().split(" ");
            int v = Integer.parseInt(parts[0]);
            int u = Integer.parseInt(parts[1]);
            int w = Integer.parseInt(parts[2]);
            if (G[v] == null)
                G[v] = new ArrayList<Integer>();
            G[v].add(u);
            if (GT[u] == null)
                GT[u] = new ArrayList<Integer>();
            GT[u].add(v);
            if (W[u] == null)
                W[u] = new ArrayList<Integer>();
            W[u].add(w);
        }
        dFSO(s);
        if (isUsed[t]) {
            for (int i = 0; i <= N; i++)
                uvw[i] = Integer.MAX_VALUE;
            uvw[s] = 0;
            o.pop();
            while (o.size() > 0) {
                int j = o.pop();
                for (int i = 0; i < GT[j].size(); i++) {
                    int k = GT[j].get(i);
                    if (isUsed[k] && uvw[j] > uvw[k] + W[j].get(i))
                        uvw[j] = uvw[k] + W[j].get(i);
                }
            }
            out.println(uvw[t]);
        } else {
            out.println("Unreachable");
        }
        out.close();
    }

    private static void dFSO(int v) {
        isUsed[v] = true;
        if (G[v] != null)
            for (int i = 0; i < G[v].size(); i++) {
                int u = G[v].get(i);
                if (!isUsed[u])
                    dFSO(u);
            }
        o.push(v);
    }
}
