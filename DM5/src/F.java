import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class F {
    private static ArrayList<Integer>[] stg;
    private static int[][] residualNet;
    private static boolean[] isUsed;

    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(new FileReader("circulation.in"));
        PrintWriter out = new PrintWriter("circulation.out");
        int n = in.nextInt() + 3;
        int m = in.nextInt();
        stg = new ArrayList[n];
        isUsed = new boolean[n];
        residualNet = new int[n][n];
        int[][] min = new int[n][n];
        int[] U = new int[m];
        int[] V = new int[m];
        for (int i = 0; i < n; i++) {
            stg[i] = new ArrayList<Integer>();
        }
        int maxC = 0;
        for (int i = 0; i < m; i++) {
            U[i] = in.nextInt() + 1;
            V[i] = in.nextInt() + 1;
            int l = in.nextInt();
            min[U[i]][V[i]] = l;
            int c = in.nextInt() - l;
            if (c > maxC)
                maxC = c;
            if (l > maxC)
                maxC = l;
            stg[U[i]].add(V[i]);
            stg[V[i]].add(U[i]);
            residualNet[U[i]][V[i]] = c;
            stg[1].add(V[i]);
            stg[V[i]].add(1);
            residualNet[1][V[i]] += l;
            stg[U[i]].add(n - 1);
            stg[n - 1].add(U[i]);
            residualNet[U[i]][n - 1] += l;
        }
        int minC = 1;
        while (minC * 2 <= maxC)
            minC *= 2;
        while (minC > 0) {
            while (dfs(1, n - 1, Integer.MAX_VALUE, minC) > 0)
                for (int j = 1; j < n; j++)
                    isUsed[j] = false;
            for (int j = 1; j < n; j++)
                isUsed[j] = false;
            minC /= 2;
        }
        for (int v : stg[1])
            if (residualNet[1][v] > 0) {
                out.println("NO");
                out.close();
                return;
            }
        out.println("YES");
        for (int i = 0; i < m; i++)
            out.println(min[U[i]][V[i]] + residualNet[V[i]][U[i]]);
        out.close();
    }

    private static int dfs(int u, int t, int curF, int minC) {
        if (u == t)
            return curF;
        if (isUsed[u])
            return 0;
        isUsed[u] = true;
        for (int v : stg[u])
            if (residualNet[u][v] >= minC) {
                int r = dfs(v, t, Math.min(curF, residualNet[u][v]), minC);
                if (r > 0) {
                    residualNet[u][v] -= r;
                    residualNet[v][u] += r;
                    return r;
                }
            }
        return 0;
    }
}