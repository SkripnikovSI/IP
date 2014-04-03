import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class A {

    private static ArrayList<Integer>[] G;
    private static int[] m;
    private static boolean[] isUsed;


    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(new FileReader("components.in"));
        PrintWriter out = new PrintWriter("components.out");
        int N = in.nextInt();
        int M = in.nextInt();
        G = new ArrayList[N + 1];
        m = new int[N + 1];
        isUsed = new boolean[N + 1];
        for (int i = 0; i < M; i++) {
            int v = in.nextInt();
            int u = in.nextInt();
            if (G[v] == null)
                G[v] = new ArrayList<Integer>();
            if (G[u] == null)
                G[u] = new ArrayList<Integer>();
            G[v].add(u);
            G[u].add(v);
        }
        int k = 1;
        for (int i = 1; i <= N; i++)
            if (!isUsed[i])
                dFSO(i, k++);
        out.println(k - 1);
        for (int i = 1; i <= N; i++)
            out.print(m[i] + " ");
        out.close();
    }

    private static void dFSO(int v, int k) {
        isUsed[v] = true;
        if (G[v] != null)
            for (int i = 0; i < G[v].size(); i++) {
                int u = G[v].get(i);
                if (!isUsed[u])
                    dFSO(u, k);
            }
        m[v] = k;
    }
}
