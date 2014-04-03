import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class A {
    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(new FileReader("spantree.in"));
        PrintWriter out = new PrintWriter("spantree.out");
        int n = in.nextInt();
        boolean[] inTree = new boolean[n];
        int[] X = new int[n];
        int[] Y = new int[n];
        for (int i = 0; i < n; i++) {
            X[i] = in.nextInt();
            Y[i] = in.nextInt();
        }
        double w = 0;
        int[] g = new int[n];
        int[] min = new int[n];
        inTree[0] = true;
        for (int j = 1; j < n; j++)
            min[j] = (X[0] - X[j]) * (X[0] - X[j]) + (Y[0] - Y[j]) * (Y[0] - Y[j]);
        for (int i = 0; i < n - 1; i++) {
            float minimum = Integer.MAX_VALUE;
            int minimumV = 0;
            for (int j = 0; j < n; j++)
                if (!inTree[j] && minimum > min[j]) {
                    minimum = min[j];
                    minimumV = j;
                }
            w += Math.sqrt(minimum);
            inTree[minimumV] = true;
            for (int j = 0; j < n; j++)
                g[j] = (X[minimumV] - X[j]) * (X[minimumV] - X[j]) + (Y[minimumV] - Y[j]) * (Y[minimumV] - Y[j]);
            for (int j = 0; j < n; j++)
                if (!inTree[j] && min[j] > g[j])
                    min[j] = g[j];
        }
        out.println(w);
        out.close();
    }
}