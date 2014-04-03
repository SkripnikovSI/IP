import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class A {
    private static ArrayList<Integer>[] g;
    private static int[] matching;
    private static boolean[] isUsed;

    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(new FileReader("matching.in"));
        PrintWriter out = new PrintWriter("matching.out");
        int n = in.nextInt();
        int m = in.nextInt();
        int k = in.nextInt();
        int N = n + m + 1;
        g = new ArrayList[N];
        matching = new int[N];
        isUsed = new boolean[N];
        for (int i = 0; i < N; i++)
            g[i] = new ArrayList<Integer>();
        for (int i = 0; i < k; i++) {
            int u = in.nextInt();
            int v = in.nextInt() + n;
            g[u].add(v);
            g[v].add(u);
        }
        for (int i = 1; i < n + 1; i++) {
            dfs(i);
            for (int j = 1; j < N; j++)
                isUsed[j] = false;
        }
        int s = 0;
        for (int i = 1; i < N; i++)
            if (matching[i] != 0)
                ++s;
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
}
