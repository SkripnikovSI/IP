import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class E {
    private static ArrayList<ArrayList<Integer>> ans;
    private static ArrayList<Integer>[] g;
    private static ArrayList<Integer>[] gp;
    private static long[][] residualNet;
    private static Edge[][] edges;
    private static boolean[] isUsed;

    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(new FileReader("decomposition.in"));
        PrintWriter out = new PrintWriter("decomposition.out");
        int n = in.nextInt() + 1;
        int m = in.nextInt();
        g = new ArrayList[n];
        gp = new ArrayList[n];
        isUsed = new boolean[n];
        residualNet = new long[n][n];
        edges = new Edge[n][n];
        ans = new ArrayList<ArrayList<Integer>>();
        for (int i = 0; i < n; i++) {
            g[i] = new ArrayList<Integer>();
            gp[i] = new ArrayList<Integer>();
        }
        long maxC = 0;
        for (int i = 0; i < m; i++) {
            int u = in.nextInt();
            int v = in.nextInt();
            int c = in.nextInt();
            g[u].add(v);
            g[v].add(u);
            gp[u].add(v);
            residualNet[u][v] += c;
            if (residualNet[u][v] > maxC)
                maxC = residualNet[u][v];
            edges[u][v] = new Edge(i + 1, c, edges[u][v]);
        }
        long minC = 1;
        while (minC * 2 <= maxC)
            minC *= 2;
        while (minC > 0) {
            while (dfs(1, n - 1, Long.MAX_VALUE, minC) > 0)
                for (int j = 1; j < n; j++)
                    isUsed[j] = false;
            for (int j = 1; j < n; j++)
                isUsed[j] = false;
            minC /= 2;
        }

        int k = 0;
        long r = 1;
        minC = 1;
        while (minC * 2 <= maxC)
            minC *= 2;
        while (minC > 0) {
            r = 1;
            while (r > 0) {
                ArrayList<Integer> list = new ArrayList<Integer>();
                list.add(0);
                ans.add(list);
                r = dfsD(k++, 1, n - 1, Long.MAX_VALUE, minC);
                for (int j = 1; j < n; j++)
                    isUsed[j] = false;
                if (r == 0) {
                    k--;
                    ans.remove(ans.size() - 1);
                }
            }
            minC /= 2;
        }

        out.println(ans.size());
        for (ArrayList<Integer> list : ans) {
            out.print(list.get(0) + " " + (list.size() - 1));
            for (int i = list.size() - 1; i > 0; i--)
                out.print(" " + list.get(i));
            out.println();
        }
        out.close();
    }

    private static long dfs(int u, int t, long curF, long minC) {
        if (u == t)
            return curF;
        if (isUsed[u])
            return 0;
        isUsed[u] = true;
        for (int v : g[u])
            if (residualNet[u][v] >= minC) {
                long r = dfs(v, t, Math.min(curF, residualNet[u][v]), minC);
                if (r > 0) {
                    residualNet[u][v] -= r;
                    residualNet[v][u] += r;
                    return r;
                }
            }
        return 0;
    }

    private static long dfsD(int k, int u, int t, long curF, long minC) {
        if (u == t)
            return curF;
        if (isUsed[u])
            return 0;
        isUsed[u] = true;
        for (int v : gp[u])
            if (edges[u][v] != null && residualNet[v][u] >= minC) {
                long r = dfsD(k, v, t, Math.min(curF, edges[u][v].c), minC);
                if (r > 0) {
                    ans.get(k).set(0, (int) r);
                    ans.get(k).add(edges[u][v].number);
                    residualNet[v][u] -= r;
                    edges[u][v].c -= r;
                    if (edges[u][v].c == 0)
                        edges[u][v] = edges[u][v].next;
                    return r;
                }
            }
        return 0;
    }

    private static class Edge {
        private Edge next;
        private int c;
        private int number;

        Edge(int number, int c, Edge next) {
            this.number = number;
            this.c = c;
            this.next = next;
        }
    }
}