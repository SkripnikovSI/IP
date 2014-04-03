import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class C {

    private static ArrayList<Integer>[] G;
    private static int timer = 0;
    private static int[] time;
    private static int[] m;
    private static boolean[] isUsed;
    private static ArrayList<Integer> answer;


    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(new FileReader("points.in"));
        PrintWriter out = new PrintWriter("points.out");
        int N = in.nextInt();
        int M = in.nextInt();
        G = new ArrayList[N + 1];
        answer = new ArrayList<Integer>();
        time = new int[N + 1];
        m = new int[N + 1];
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
        Integer a[] = new Integer[answer.size()];
        a = answer.toArray(a);
        Arrays.sort(a);
        out.println(a.length);
        for (Integer anA : a) out.print(anA + " ");
        out.close();
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
                        answer.add(v);
                        isInAns = true;
                    }
                    number++;
                }
            }
            if (parent == 0 && number > 1) {
                answer.add(v);
            }
        }
    }
}
