
import java.io.*;
import java.util.ArrayList;

public class F {
    public static int[] p;
    public static ArrayList<Integer> path;
    public static ArrayList<Integer> u;
    public static ArrayList<Integer> v;
    public static ArrayList<Long> w;
    public static long[] d;
    public static int n;

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("negcycle.in"));
        PrintWriter out = new PrintWriter("negcycle.out");

        n = Integer.parseInt(in.readLine());

        path = new ArrayList<Integer>();
        u = new ArrayList<Integer>();
        v = new ArrayList<Integer>();
        w = new ArrayList<Long>();
        p = new int[n];
        for (int i = 0; i < n; i++) {
            String[] parts = in.readLine().split(" ");
            for (int j = 0; j < n; j++) {
                long weight = Long.parseLong(parts[j]);
                if (weight > -20000 && weight < 20000) {
                    u.add(i);
                    v.add(j);
                    w.add(weight);
                }
            }
        }

        d = new long[n];

        int top = -1;
        for (int i = 0; i < n; i++) {
            top = -1;
            for (int j = 0; j < u.size(); j++) {
                if (d[v.get(j)] > d[u.get(j)] + w.get(j)) {
                    d[v.get(j)] = Math.max(Integer.MIN_VALUE / 2, d[u.get(j)] + w.get(j));
                    p[v.get(j)] = u.get(j);
                    top = v.get(j);
                }
            }
        }
        if (top == -1) {
            out.println("NO");
        } else {
            for (int i = 0; i < n; ++i)
                top = p[top];
            for (int c = top; !(c == top && path.size() > 1); c = p[c])
                path.add(c);
            path.add(path.get(0));
            out.println("YES");
            out.println(path.size());
            for (int i = path.size() - 1; i >= 0; i--)
                out.print(path.get(i) + 1 + " ");
        }
        out.close();
    }
}