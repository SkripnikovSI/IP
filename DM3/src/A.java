
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class A {

    public static ArrayList<Integer>[] g;
    public static boolean[] isUsed;
    public static int[] minDeep;
    public static Queue<Integer> q;

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("pathbge1.in"));
        PrintWriter out = new PrintWriter("pathbge1.out");
        String parts[] = in.readLine().split(" ");
        int n = Integer.parseInt(parts[0]);
        int m = Integer.parseInt(parts[1]);
        g = new ArrayList[n + 1];
        for (int i = 1; i < n + 1; i++)
            g[i] = new ArrayList<Integer>();
        isUsed = new boolean[n + 1];
        minDeep = new int[n + 1];
        q = new LinkedList<Integer>();
        for (int i = 0; i < m; i++) {
            parts = in.readLine().split(" ");
            int u = Integer.parseInt(parts[0]);
            int v = Integer.parseInt(parts[1]);
            g[u].add(v);
            g[v].add(u);
        }
        q.add(1);
        isUsed[1] = true;
        while (!q.isEmpty())
            BFS(q.poll());
        for (int i = 1; i < n + 1; i++)
            out.print(minDeep[i] + " ");
        out.close();
    }

    public static void BFS(int u) {
        for (int i = 0; i < g[u].size(); i++) {
            int v = g[u].get(i);
            if (isUsed[v]) {
                if (minDeep[u] + 1 < minDeep[v])
                    minDeep[v] = minDeep[u] + 1;
            } else {
                q.add(v);
                isUsed[v] = true;
                minDeep[v] = minDeep[u] + 1;
            }
        }
    }
}