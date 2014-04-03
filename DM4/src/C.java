import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

public class C {
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("mindiff.in"));
        PrintWriter out = new PrintWriter("mindiff.out");
        String[] parts = in.readLine().split(" ");
        int n = Integer.parseInt(parts[0]);
        int m = Integer.parseInt(parts[1]);
        Edge[] edges = new Edge[m];
        for (int i = 0; i < m; i++) {
            parts = in.readLine().split(" ");
            int u = Integer.parseInt(parts[0]);
            int v = Integer.parseInt(parts[1]);
            int weight = Integer.parseInt(parts[2]);
            edges[i] = new Edge(u, v, weight);
        }
        Arrays.sort(edges);
        long min = Integer.MAX_VALUE;
        for (int i = 0; i < m; i++) {
            DisjointSetUnion dsu = new DisjointSetUnion(n + 1);
            dsu.union(edges[i].p1, edges[i].p2);
            int v = edges[i].p1;
            long start = edges[i].w;
            long end = start;
            for (int j = i + 1; j < m; j++)
                if (dsu.get(edges[j].p1) != dsu.get(edges[j].p2)) {
                    dsu.union(edges[j].p1, edges[j].p2);
                    end = edges[j].w;
                    if (dsu.getSize(v) == n)
                        break;
                }
            if (dsu.getSize(v) == n)
                if (min > end - start)
                    min = end - start;
        }
        if (min == Integer.MAX_VALUE) {
            out.println("NO");
        } else {
            out.println("YES");
            out.println(min);
        }
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

    private static class DisjointSetUnion {

        private int[] parent;
        private int[] size;

        DisjointSetUnion(int maxSize) {
            parent = new int[maxSize];
            size = new int[maxSize];
            for (int i = 1; i < maxSize; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        private int getSize(int value) {
            value = get(value);
            return size[value];
        }

        public int get(int value) {
            if (value == parent[value]) {
                return value;
            }
            return parent[value] = get(parent[value]);
        }

        public void union(int x, int y) {
            x = get(x);
            y = get(y);
            if (x != y) {
                parent[y] = x;
                size[x] += size[y];
            }
        }
    }
}