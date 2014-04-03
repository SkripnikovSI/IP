import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class F {

    private static ArrayList<Integer>[] G;
    private static Stack<Integer> o = new Stack<Integer>();
    private static boolean[] isUsed;


    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(new FileReader("hamiltonian.in"));
        PrintWriter out = new PrintWriter("hamiltonian.out");
        int N = in.nextInt();
        int M = in.nextInt();
        G = new ArrayList[N + 1];
        isUsed = new boolean[N + 1];
        for (int i = 0; i < M; i++) {
            int v = in.nextInt();
            int u = in.nextInt();
            if (G[v] == null)
                G[v] = new ArrayList<Integer>();
            G[v].add(u);
        }
        for (int i = 1; i <= N; i++)
            if (!isUsed[i])
                dFSO(i);
        boolean fl = true;
        l:
        while (o.size() > 1) {
            int v = o.pop();
            int u = o.peek();
            if (G[v] != null)
                for (int i = 0; i < G[v].size(); i++)
                    if (u == G[v].get(i))
                        continue l;
            out.println("NO");
            fl = false;
            break;
        }
        if (fl)
            out.println("YES");
        out.close();
    }

    private static void dFSO(int v) {
        isUsed[v] = true;
        if (G[v] != null)
            for (int i = 0; i < G[v].size(); i++) {
                int u = G[v].get(i);
                if (!isUsed[u])
                    dFSO(u);
            }
        o.push(v);
    }
}
