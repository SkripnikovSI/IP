import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class C {
    private static ArrayList<Integer>[] g;
    private static long[][] residualNet;
    private static boolean[] isUsed;
    private static ArrayList<Integer> ans;

    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(new FileReader("cut.in"));
        PrintWriter out = new PrintWriter("cut.out");
        int n = in.nextInt() + 1;
        int m = in.nextInt();
        g = new ArrayList[n];
        isUsed = new boolean[n];
        residualNet = new long[n][n];
        ans = new ArrayList<Integer>();
        for (int i = 0; i < n; i++)
            g[i] = new ArrayList<Integer>();
        long maxC = 0;
        for (int i = 0; i < m; i++) {
            int u = in.nextInt();
            int v = in.nextInt();
            int c = in.nextInt();
            if (c > maxC)
                maxC = c;
            g[u].add(v);
            g[v].add(u);
            residualNet[u][v] = c;
            residualNet[v][u] = c;
        }
        long minC = 1;
        while (minC * 2 <= maxC)
            minC *= 2;
        while (minC > 0) {
            long r = 1;
            while (r > 0) {
                r = dfs(1, n - 1, Long.MAX_VALUE, minC);
                for (int j = 1; j < n; j++)
                    isUsed[j] = false;
            }
            minC /= 2;
        }
        dfsAns(1);
        out.println(ans.size());
        for (int u : ans)
            out.print(u + " ");
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

    private static void dfsAns(int u) {
        isUsed[u] = true;
        ans.add(u);
        for (int v : g[u])
            if (!isUsed[v] && residualNet[u][v] > 0)
                dfsAns(v);
    }
}