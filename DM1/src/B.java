import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Stack;

public class B {

    private static ArrayList<Integer>[] m;
    private static ArrayList<Integer>[] mt;
    private static Stack<Integer> o = new Stack<Integer>();
    private static int[] col;
    private static int N;
    private static int M;
    private static int[] com;

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("cond.in"));
        PrintWriter out = new PrintWriter("cond.out");
        String parts[] = in.readLine().split(" ");
        N = Integer.parseInt(parts[0]);
        M = Integer.parseInt(parts[1]);
        m = new ArrayList[N + 1];
        mt = new ArrayList[N + 1];
        col = new int[N + 1];
        com = new int[N + 1];
        for (int i = 0; i < M; i++) {
            parts = in.readLine().split(" ");
            int v = Integer.parseInt(parts[0]);
            int u = Integer.parseInt(parts[1]);
            if (m[v] == null)
                m[v] = new ArrayList<Integer>();
            m[v].add(u);
            if (mt[u] == null)
                mt[u] = new ArrayList<Integer>();
            mt[u].add(v);
        }
        for (int i = 1; i <= N; i++)
            if (col[i] == 0)
                dFSO(i);
        for (int i = 1; i <= N; i++)
            col[i] = 0;
        int n = 1;
        while(o.size() > 0) {
            int v = o.pop();
            if (col[v] == 0) {
                dFS(v, n);
                n++;
            }
        }
        out.println(n-1);
        for (int i = 1; i <= N; i++)
            out.print(com[i] + " ");
        out.close();
    }

    private static void dFSO(int v) {
        if (m[v] != null) {
            col[v] = 1;
            for (int i = 0; i < m[v].size(); i++) {
                int u = m[v].get(i);
                if (col[u] == 0)
                    dFSO(u);
            }
        }
        col[v] = 2;
        o.push(v);
    }

    private static void dFS(int v, int n) {
        if (mt[v] != null) {
            col[v] = 1;
            for (int i = 0; i < mt[v].size(); i++) {
                int u = mt[v].get(i);
                if (col[u] == 0)
                    dFS(u, n);
            }
        }
        com[v] = n;
        col[v] = 2;
    }
}
