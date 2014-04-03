import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class A {

    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(new FileReader("assignment.in"));
        PrintWriter out = new PrintWriter("assignment.out");
        int n = in.nextInt() + 1;
        int[][] c = new int[n][n];
        for (int i = 1; i < n; i++)
            for (int j = 1; j < n; j++)
                c[i][j] = in.nextInt();
        int[] u = new int[n];
        int[] v = new int[n];
        int[] m = new int[n];
        int[] path = new int[n];
        int[] min = new int[n];
        boolean[] isUsed = new boolean[n];
        for (int i = 1; i < n; ++i) {
            m[0] = i;
            int v1 = 0;
            for (int j = 0; j < n; ++j) {
                min[j] = Integer.MAX_VALUE;
                isUsed[j] = false;
            }
            do {
                isUsed[v1] = true;
                int u1 = m[v1];
                int d = Integer.MAX_VALUE;
                int v2 = 0;
                for (int j = 1; j < n; ++j)
                    if (!isUsed[j]) {
                        int cur = c[u1][j] - u[u1] - v[j];
                        if (cur < min[j]) {
                            min[j] = cur;
                            path[j] = v1;
                        }
                        if (min[j] < d) {
                            d = min[j];
                            v2 = j;
                        }
                    }
                for (int j = 0; j < n; ++j)
                    if (isUsed[j]) {
                        u[m[j]] += d;
                        v[j] -= d;
                    } else
                        min[j] -= d;
                v1 = v2;
            } while (m[v1] != 0);
            do {
                m[v1] = m[path[v1]];
                v1 = path[v1];
            } while (v1 != 0);
        }
        out.println(-v[0]);
        int[] a = new int[n];
        for (int i = 1; i < n; ++i)
            a[m[i]] = i;
        for (int i = 1; i < n; ++i)
            out.println(i + " " + a[i]);
        out.close();
    }
}
