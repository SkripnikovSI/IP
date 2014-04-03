import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class D {
    private static ArrayList<Integer>[] g;
    private static int[] matching;
    private static boolean[] isUsed;

    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(new FileReader("paths.in"));
        PrintWriter out = new PrintWriter("paths.out");
        int n = in.nextInt() + 1;
        int m = in.nextInt();
        g = new ArrayList[n];
        matching = new int[n];
        isUsed = new boolean[n];
        for (int i = 0; i < n; i++)
            g[i] = new ArrayList<Integer>();
        for (int i = 0; i < m; i++) {
            int u = in.nextInt();
            int v = in.nextInt();
            g[u].add(v);
        }
        for (int i = 1; i < n; i++) {
            dfs(i);
            for (int j = 1; j < n; j++)
                isUsed[j] = false;
        }
        int s = 0;
        for (int i = 1; i < n; i++)
            if (!isUsed[i] && dfsAns(i))
                s++;
        out.println(s);
        out.close();
    }

    private static boolean dfs(int u) {
        if (isUsed[u])
            return false;
        isUsed[u] = true;
        for (int v : g[u]) {
            if (matching[v] == 0 || dfs(matching[v])) {
                matching[v] = u;
                return true;
            }
        }
        return false;
    }

    private static boolean dfsAns(int u) {
        isUsed[u] = true;
        return matching[u] == 0 || !isUsed[matching[u]] && dfsAns(matching[u]);
    }
}
