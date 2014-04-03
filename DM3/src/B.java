
import java.io.*;
import java.util.ArrayList;

public class B {

    public static ArrayList<Integer>[] g;
    public static ArrayList<Long>[] w;
    public static long[] pw;
    public static boolean[] isUsed;
    public static int isUsedNumber = 0;

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("pathmgep.in"));
        PrintWriter out = new PrintWriter("pathmgep.out");
        String parts[] = in.readLine().split(" ");
        int n = Integer.parseInt(parts[0]);
        int s = Integer.parseInt(parts[1]);
        int f = Integer.parseInt(parts[2]);
        w = new ArrayList[n + 1];
        g = new ArrayList[n + 1];
        pw = new long[n + 1];
        isUsed = new boolean[n + 1];
        for (int i = 0; i < n + 1; i++) {
            w[i] = new ArrayList<Long>();
            g[i] = new ArrayList<Integer>();
            pw[i] = Long.MAX_VALUE;
        }
        pw[s] = 0;
        for (int i = 0; i < n; i++) {
            parts = in.readLine().split(" ");
            for (int j = 0; j < n; j++) {
                long weight = Long.parseLong(parts[j]);
                if (weight >= 0) {
                    g[i + 1].add(j + 1);
                    w[i + 1].add(weight);
                }
            }
        }
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
        if (pw[f] == Long.MAX_VALUE)
            out.println(-1);
        else
            out.println(pw[f]);
        out.close();
    }
}