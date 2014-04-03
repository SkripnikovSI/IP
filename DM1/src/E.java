import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Stack;

public class E {

    private static ArrayList<Integer>[] m;
    private static Stack<Integer> s = new Stack<Integer>();
    private static int[] c;
    private static int N;
    private static int M;
    private static boolean toSteck = false;
    private static int loop = 0;

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("cycle.in"));
        PrintWriter out = new PrintWriter("cycle.out");
        String parts[] = in.readLine().split(" ");
        N = Integer.parseInt(parts[0]);
        M = Integer.parseInt(parts[1]);
        m = new ArrayList[N + 1];
        c = new int[N + 1];
        for (int i = 0; i < M; i++) {
            parts = in.readLine().split(" ");
            int v = Integer.parseInt(parts[0]);
            int u = Integer.parseInt(parts[1]);
            if (m[v] == null)
                m[v] = new ArrayList<Integer>();
            m[v].add(u);
        }
        for (int i = 1; i <= N; i++)
            if (c[i] == 0)
                dFS(i);
        if (s.isEmpty()) {
            out.print("NO");
        } else {
            out.println("YES");
            while(s.size() > 0)
                out.print(s.pop() + " ");
        }
        out.close();
    }

    private static void dFS(int v) {
        if(loop != 0)
            return;
        if (m[v] != null) {
            c[v] = 1;
            for (int i = 0; i < m[v].size(); i++) {
                int u = m[v].get(i);
                if(toSteck)
                    break;
                if (c[u] == 0) {
                    dFS(u);
                } else if (c[u] == 1) {
                    toSteck = true;
                    loop = u;
                    break;
                }
            }
        }
        c[v] = 2;
        if(toSteck) {
            s.push(v);
            if(v == loop)
                toSteck = false;
        }
    }
}
