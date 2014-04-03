import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class D {

    private static ArrayList<Integer>[] G;
    private static boolean[] isUsed;
    private static boolean[] color;
    private static String answer = "YES";


    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(new FileReader("bipartite.in"));
        PrintWriter out = new PrintWriter("bipartite.out");
        int N = in.nextInt();
        int M = in.nextInt();
        G = new ArrayList[N + 1];
        color = new boolean[N + 1];
        isUsed = new boolean[N + 1];
        for (int i = 0; i < M; i++) {
            int v = in.nextInt();
            int u = in.nextInt();
            if (G[v] == null) {
                G[v] = new ArrayList<Integer>();
            }
            if (G[u] == null) {
                G[u] = new ArrayList<Integer>();
            }
            G[v].add(u);
            G[u].add(v);
        }
        for (int i = 1; i <= N; i++)
            if (!isUsed[i])
                dFS(i, 0);
        out.println(answer);
        out.close();
    }

    private static void dFS(int v, int parent) {
        isUsed[v] = true;
        color[v] = !color[parent];
        if (G[v] != null)
            for (int i = 0; i < G[v].size(); i++) {
                int u = G[v].get(i);
                if (u == parent)
                    continue;
                if (isUsed[u]) {
                    if (color[v] == color[u])
                        answer = "NO";
                } else
                    dFS(u, v);
            }
    }
}
