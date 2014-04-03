import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.PriorityQueue;


public class B {
    private static ArrayList<Integer>[] g;
    private static ArrayList<Integer>[] w;

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("spantree2.in"));
        PrintWriter out = new PrintWriter("spantree2.out");
        String[] parts = in.readLine().split(" ");
        int n = Integer.parseInt(parts[0]);
        int m = Integer.parseInt(parts[1]);
        g = new ArrayList[n + 1];
        w = new ArrayList[n + 1];
        for (int i = 0; i < n + 1; i++) {
            g[i] = new ArrayList<Integer>();
            w[i] = new ArrayList<Integer>();
        }
        for (int i = 0; i < m; i++) {
            parts = in.readLine().split(" ");
            int u = Integer.parseInt(parts[0]);
            int v = Integer.parseInt(parts[1]);
            int weight = Integer.parseInt(parts[2]);
            g[u].add(v);
            w[u].add(weight);
            g[v].add(u);
            w[v].add(weight);
        }
        boolean[] inTree = new boolean[n + 1];
        long W = 0;
        PriorityQueue<Edge> pq = new PriorityQueue<Edge>();
        inTree[1] = true;
        for (int i = 0; i < g[1].size(); i++)
            pq.add(new Edge(1, g[1].get(i), w[1].get(i)));
        for (int i = 0; i < n - 1; i++) {
            Edge e = pq.poll();
            while (inTree[e.p2])
                e = pq.poll();
            W += e.w;
            inTree[e.p2] = true;
            for (int j = 0; j < g[e.p2].size(); j++)
                if (!inTree[g[e.p2].get(j)])
                    pq.add(new Edge(e.p2, g[e.p2].get(j), w[e.p2].get(j)));
        }
        out.println(W);
        out.close();
    }

    private static class Edge implements Comparable {
        final int p1;
        final int p2;
        final long w;

        Edge(int p1, int p2, long w) {
            this.p1 = p1;
            this.p2 = p2;
            this.w = w;
        }

        @Override
        public int compareTo(Object o) {
            return (int) (this.w - ((Edge) o).w);
        }
    }
}