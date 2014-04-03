import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class B {
    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(new FileReader("mincost.in"));
        PrintWriter out = new PrintWriter("mincost.out");
        int n = in.nextInt() + 1;
        int m = in.nextInt();
        ArrayList<Integer>[] G = new ArrayList[n];
        ArrayList<Integer>[] C = new ArrayList[n];
        ArrayList<Integer>[] W = new ArrayList[n];
        for (int i = 0; i < n; i++)
            G[i] = new ArrayList<Integer>();
        for (int i = 0; i < m; i++) {
            int u = in.nextInt();
            G[u].add(in.nextInt());
            C[u].add(in.nextInt());
            W[u].add(in.nextInt());
        }

        out.close();
    }
}
