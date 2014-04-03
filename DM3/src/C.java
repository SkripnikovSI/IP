
import java.io.*;
import java.util.ArrayList;

public class C {

    public static ArrayList<Integer>[] g;
    public static ArrayList<Long>[] w;
    public static long[] pw;
    public static boolean[] isUsed;
    public static int isUsedNumber = 0;

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("pathsg.in"));
        PrintWriter out = new PrintWriter("pathsg.out");
        String parts[] = in.readLine().split(" ");
        int n = Integer.parseInt(parts[0]);
        int m = Integer.parseInt(parts[1]);
        w = new ArrayList[n + 1];
        g = new ArrayList[n + 1];
        pw = new long[n + 1];
        isUsed = new boolean[n + 1];
        for (int i = 0; i < n + 1; i++) {
            w[i] = new ArrayList<Long>();
            g[i] = new ArrayList<Integer>();
        }
        for (int i = 0; i < m; i++) {
            parts = in.readLine().split(" ");
            int u = Integer.parseInt(parts[0]);
            int v = Integer.parseInt(parts[1]);
            long weight = Long.parseLong(parts[2]);
            g[u].add(v);
            w[u].add(weight);
        }
        for (int j = 1; j < n + 1; j++) {
            isUsed = new boolean[n + 1];
            for (int q = 0; q < n + 1; q++)
                pw[q] = Long.MAX_VALUE;
            pw[j] = 0;
            isUsedNumber = 0;
            while (isUsedNumber < n) {
                int minNU = 0;
                for (int i = 1; i < n + 1; i++) {
                    if (!isUsed[i])
                        if (pw[minNU] > pw[i])
                            minNU = i;
                }
                isUsed[minNU] = true;
                isUsedNumber++;
                for (int i = 0; i < g[minNU].size(); i++) {
                    int v = g[minNU].get(i);
                    if (pw[minNU] != Long.MAX_VALUE)
                        pw[v] = Math.min(pw[v], pw[minNU] + w[minNU].get(i));
                }
            }
            for (int i = 1; i < n + 1; i++) {
                if (pw[i] == Long.MAX_VALUE)
                    out.print(-1 + " ");
                else
                    out.print(pw[i] + " ");
            }
            out.println();
        }
        out.close();
    }
}