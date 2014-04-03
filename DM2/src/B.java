import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class B {

    private static ArrayList[] G;
    private static ArrayList<Integer>[] Num;
    private static int timer = 0;
    private static int[] time;
    private static int[] m;
    private static boolean[] isUsed;
    private static ArrayList<Integer> answer;


    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(new FileReader("bridges.in"));
        PrintWriter out = new PrintWriter("bridges.out");
        int N = in.nextInt();
        int M = in.nextInt();
        G = new ArrayList[N + 1];
        Num = new ArrayList[N + 1];
        answer = new ArrayList<Integer>();
        time = new int[N + 1];
        m = new int[N + 1];
        isUsed = new boolean[N + 1];
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
        Integer a[] = new Integer[answer.size()];
        a = answer.toArray(a);
        Arrays.sort(a);
        out.println(a.length);
        for (int i = 0; i < a.length; i++)
            if(i < 1 || a[i] != a[i-1])
                out.print(a[i]+" ");
        out.close();
    }

    private static void dFS(int v, int parent) {
        isUsed[v] = true;
        time[v] = m[v] = timer++;
        if (G[v] != null)
            for (int i = 0; i < G[v].size(); i++) {
                int u = (Integer)G[v].get(i);
                if (u == parent)
                    continue;
                if (isUsed[u]) {
                    m[v] = Math.min(m[v], time[u]);
                } else {
                    dFS(u, v);
                    m[v] = Math.min(m[v], m[u]);
                    if (m[u] > time[v])
                        answer.add(Num[v].get(i));
                }
            }
    }
}
