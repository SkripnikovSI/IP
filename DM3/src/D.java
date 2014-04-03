
import java.io.*;
import java.util.ArrayList;
import java.util.TreeSet;

public class D {

    public static ArrayList<Integer>[] g;
    public static ArrayList<Integer>[] w;
    public static Point[] p;
    public static int isUsedNumber = 0;

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("pathbgep.in"));
        PrintWriter out = new PrintWriter("pathbgep.out");
        String parts[] = in.readLine().split(" ");
        int n = Integer.parseInt(parts[0]);
        int m = Integer.parseInt(parts[1]);
        TreeSet<Point> pq = new TreeSet<Point>();
        w = new ArrayList[n + 1];
        g = new ArrayList[n + 1];
        p = new Point[n + 1];

        for (int i = 1; i < n + 1; i++) {
            w[i] = new ArrayList<Integer>();
            g[i] = new ArrayList<Integer>();
            p[i] = new Point(i, Integer.MAX_VALUE);
        }

        p[1].w = 0;
        for (int i = 1; i < n + 1; i++)
            pq.add(p[i]);

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

        while (isUsedNumber < n) {
            Point point = pq.pollFirst();
            point.isAlive = false;
            isUsedNumber++;
            for (int i = 0; i < g[point.top].size(); i++) {
                int v = g[point.top].get(i);
                if (point.w != Integer.MAX_VALUE && p[v].isAlive) {
                    pq.remove(p[v]);
                    p[v].w = Math.min(p[v].w, point.w + w[point.top].get(i));
                    pq.add(p[v]);
                }
            }
        }
        for (int i = 1; i < n + 1; i++) {
            if (p[i].w == Integer.MAX_VALUE)
                out.print(-1 + " ");
            else
                out.print(p[i].w + " ");
        }
        out.close();
    }

    public static class Point implements Comparable {
        int top;
        long w;
        boolean isAlive = true;

        Point(int top, long w) {
            this.top = top;
            this.w = w;
        }

        @Override
        public int compareTo(Object o) {
            int c = (int) (this.w - ((Point) o).w);
            if (c == 0)
                return (this.top - ((Point) o).top);
            else
                return c;
        }
    }
}