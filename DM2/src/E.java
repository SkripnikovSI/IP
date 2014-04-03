import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class E {

    private static ArrayList[] G;
    private static ArrayList<Integer>[] Num;
    private static int timer = 0;
    private static int[] time;
    private static int[] m;
    private static boolean[] isUsed;
    private static int[] colors;
    private static int maxColor = 0;

    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(new FileReader("bicone.in"));
        PrintWriter out = new PrintWriter("bicone.out");
        int N = in.nextInt();
        int M = in.nextInt();
        G = new ArrayList[N + 1];
        Num = new ArrayList[N + 1];
        time = new int[N + 1];
        m = new int[N + 1];
        isUsed = new boolean[N + 1];
        colors = new int[N + 1];
        for (int i = 0; i < M; i++) {
            int v = in.nextInt();
            int u = in.nextInt();
            if (G[v] == null) {
                G[v] = new ArrayList<Integer>();
                Num[v] = new ArrayList<Integer>();
            }
            if (G[u] == null) {
                G[u] = new ArrayList<Integer>();
                Num[u] = new ArrayList<Integer>();
            }
            G[v].add(u);
            Num[v].add(i + 1);
            G[u].add(v);
            Num[u].add(i + 1);
        }
        for (int i = 1; i <= N; i++)
            if (!isUsed[i])
                dFS(i, 0);
        for (int i = 1; i <= N; i++)
            if (colors[i] == 0)
                coloring(i, ++maxColor);
        out.println(maxColor);
        for(int i = 1; i <= N; i++)
            out.print(colors[i] + " ");
        out.close();
    }

    private static void coloring(int v, int color) {
        colors[v] = color;
        if (G[v] != null)
            for (int i = 0; i < G[v].size(); i++) {
                int u = (Integer) G[v].get(i);
                if (colors[u] == 0) {
                    if (time[u] == m[u])
                        coloring(u, ++maxColor);
                    else
                        coloring(u, color);
                }
            }
    }

    private static void dFS(int v, int parent) {
        isUsed[v] = true;
        time[v] = m[v] = timer++;
        if (G[v] != null)
            for (int i = 0; i < G[v].size(); i++) {
                int u = (Integer) G[v].get(i);
                if (u == parent)
                    continue;
                if (isUsed[u]) {
                    m[v] = Math.min(m[v], time[u]);
                } else {
                    dFS(u, v);
                    m[v] = Math.min(m[v], m[u]);
                }
            }
    }

}
