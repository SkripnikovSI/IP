
import java.io.*;
import java.util.ArrayList;

public class Main {

    public static ArrayList<Integer> u;
    public static ArrayList<Integer> v;
    public static ArrayList<Long> w;
    public static long[] d;
    public static int n;
    public static int m;
    public static int s;

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        String parts[] = in.readLine().split(" ");
        n = Integer.parseInt(parts[0]);
        m = Integer.parseInt(parts[1]);
        s = Integer.parseInt(parts[2]) - 1;

        u = new ArrayList<Integer>();
        v = new ArrayList<Integer>();
        w = new ArrayList<Long>();
        for (int i = 0; i < m; i++) {
            parts = in.readLine().split(" ");
            u.add(Integer.parseInt(parts[0]) - 1);
            v.add(Integer.parseInt(parts[1]) - 1);
            w.add(Long.parseLong(parts[2]));
        }

        d = new long[n];

        for (int i = 0; i < n; i++)
            d[i] = Long.MAX_VALUE/2;
        d[s] = 0;
        for (int i = 0; i < n - 1; i++)
            for (int j = 0; j < u.size(); j++)
                if (d[u.get(j)] != Long.MAX_VALUE && d[v.get(j)] > d[u.get(j)] + w.get(j))
                    d[v.get(j)] = d[u.get(j)] + w.get(j);
        for (int i = 0; i < n - 1; i++)
            for (int j = 0; j < u.size(); j++)
                if (d[u.get(j)] == Long.MIN_VALUE || (d[u.get(j)] != Long.MAX_VALUE && d[v.get(j)] > d[u.get(j)] + w.get(j)))
                    d[v.get(j)] = Long.MIN_VALUE;
        for (int i = 0; i < n; i++)
            if (d[i] == Long.MIN_VALUE)
                System.out.println("-");
            else if (d[i] == Long.MAX_VALUE)
                System.out.println("*");
            else
                System.out.println(d[i]);
    }
}