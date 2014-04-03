import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class E {
    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(new FileReader("chinese.in"));
        PrintWriter out = new PrintWriter("chinese.out");
        int n = in.nextInt();
        int m = in.nextInt();
        ArrayList<Edge> edges = new ArrayList<Edge>();
        ArrayList<Integer>[] g = new ArrayList[n + 1];
        for (int i = 0; i <= n; i++)
            g[i] = new ArrayList<Integer>();
        for (int i = 0; i < m; i++) {
            int u = in.nextInt();
            int v = in.nextInt();
            g[u].add(v);
            edges.add(new Edge(u, v, in.nextInt()));
        }
        boolean[] isUsed = new boolean[n + 1];
        Number number = new Number();
        dFS1(g, isUsed, number, 1);
        if (number.value == n) {
            out.println("YES");
            out.println(getWMST(edges, n, 1));
        } else
            out.println("NO");
        out.close();
    }


    private static long getWMST(ArrayList<Edge> edges, int n, int root) {
        long res = 0;
        long wMinEnteringEdge[] = new long[n + 1];
        for (int i = 0; i < n + 1; i++)
            wMinEnteringEdge[i] = Integer.MAX_VALUE;
        for (Edge edge : edges) wMinEnteringEdge[edge.v] = Math.min(edge.w, wMinEnteringEdge[edge.v]);
        for (long aWMinEnteringEdge : wMinEnteringEdge)
            if (aWMinEnteringEdge != Integer.MAX_VALUE)
                res += aWMinEnteringEdge;
        if (wMinEnteringEdge[root] != Integer.MAX_VALUE)
            res -= wMinEnteringEdge[root];
        for (Edge edge : edges)
            if (wMinEnteringEdge[edge.v] != Integer.MAX_VALUE)
                edge.w -= wMinEnteringEdge[edge.v];

        ArrayList<Integer>[] g = new ArrayList[n + 1];
        ArrayList<Integer>[] gt = new ArrayList[n + 1];
        Stack<Integer> o = new Stack<Integer>();
        boolean[] isUsed = new boolean[n + 1];

        for (int i = 0; i <= n; i++) {
            g[i] = new ArrayList<Integer>();
            gt[i] = new ArrayList<Integer>();
        }
        for (Edge edge : edges) {
            if (edge.w == 0) {
                g[edge.u].add(edge.v);
                gt[edge.v].add(edge.u);
            }
        }
        Number number = new Number();
        dFS1(g, isUsed, number, root);
        if (number.value == n)
            return res;
        isUsed = new boolean[n + 1];

        for (int i = 1; i <= n; i++)
            if (!isUsed[i])
                dFS2(g, isUsed, o, i);
        isUsed = new boolean[n + 1];
        int[] com = new int[n + 1];
        n = 1;
        while (o.size() > 0) {
            int v = o.pop();
            if (!isUsed[v]) {
                dFS3(gt, isUsed, com, n, v);
                n++;
            }
        }
        --n;
        ArrayList<Edge> newEdges = new ArrayList<Edge>();
        for (Edge edge : edges) {
            if (com[edge.u] != com[edge.v])
                newEdges.add(new Edge(com[edge.u], com[edge.v], edge.w));
        }
        res += getWMST(newEdges, n, com[root]);
        return res;
    }

    private static void dFS1(ArrayList<Integer>[] g, boolean[] isUsed, Number number, int u) {
        isUsed[u] = true;
        number.value++;
        for (int i = 0; i < g[u].size(); i++)
            if (!isUsed[g[u].get(i)])
                dFS1(g, isUsed, number, g[u].get(i));
    }

    private static void dFS2(ArrayList<Integer>[] g, boolean[] isUsed, Stack<Integer> o, int u) {
        isUsed[u] = true;
        for (int i = 0; i < g[u].size(); i++)
            if (!isUsed[g[u].get(i)])
                dFS2(g, isUsed, o, g[u].get(i));
        o.push(u);
    }

    private static void dFS3(ArrayList<Integer>[] gt, boolean[] isUsed, int[] com, int n, int u) {
        isUsed[u] = true;
        for (int i = 0; i < gt[u].size(); i++)
            if (!isUsed[gt[u].get(i)])
                dFS3(gt, isUsed, com, n, gt[u].get(i));
        com[u] = n;
    }

    private static class Number {
        int value = 0;
    }

    private static class Edge {
        int u;
        int v;
        int w;

        Edge(int u, int v, int w) {
            this.u = u;
            this.v = v;
            this.w = w;
        }
    }
}
