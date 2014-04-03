import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class F {

    private static ArrayList<Integer>[] G;
    private static ArrayList<Integer>[] Num;
    private static int timer = 0;
    private static int[] time;
    private static int[] m;
    private static boolean[] isUsed;
    private static boolean[] itIsPA;
    private static int[] colors;
    private static int maxColor = 0;


    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(new FileReader("biconv.in"));
        PrintWriter out = new PrintWriter("biconv.out");
        int N = in.nextInt();
        int M = in.nextInt();
        G = new ArrayList[N + 1];
        Num = new ArrayList[N + 1];
        time = new int[N + 1];
        m = new int[N + 1];
        isUsed = new boolean[N + 1];
        itIsPA = new boolean[N + 1];
        colors = new int[M + 1];
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
        isUsed = new boolean[N + 1];
        for (int i = 1; i <= N; i++)
            if (!isUsed[i])
                coloring(i, maxColor, 0);
        out.println(maxColor);
        for (int i = 1; i <= M; i++)
            out.print(colors[i] + " ");
        out.close();
    }

    private static void coloring(int v, int color, int parent) {
        isUsed[v] = true;
        if (G[v] != null)
            for (int i = 0; i < G[v].size(); i++) {
                int u = G[v].get(i);
                if(u == parent)
                    continue;
                if (isUsed[u]) {
                    if (time[v] >= time[u])
                        colors[Num[v].get(i)] = color;
                } else {
                    if (time[v] <= m[u]) {
                        colors[Num[v].get(i)] = ++maxColor;
                        coloring(u, maxColor, v);
                    } else {
                        colors[Num[v].get(i)] = color;
                        coloring(u, color, v);
                    }
                }
            }
    }

    private static void dFS(int v, int parent) {
        int number = 0;
        isUsed[v] = true;
        time[v] = m[v] = timer++;
        if (G[v] != null) {
            boolean isInAns = false;
            for (int i = 0; i < G[v].size(); i++) {
                int u = G[v].get(i);
                if (u == parent)
                    continue;
                if (isUsed[u]) {
                    m[v] = Math.min(m[v], time[u]);
                } else {
                    dFS(u, v);
                    m[v] = Math.min(m[v], m[u]);
                    if (m[u] >= time[v] && parent != 0 && !isInAns) {
                        itIsPA[v] = true;
                        isInAns = true;
                    }
                    number++;
                }
            }
            if (parent == 0 && number > 1)
                itIsPA[v] = true;
        }
    }
}
