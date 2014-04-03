import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class E {
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("search3.in"));
        PrintWriter out = new PrintWriter("search3.out");
        String p = in.readLine();
        String t = in.readLine();
        int z[] = new ZFunction(p, t).getZ();
        int iz[] = new IZFunction(p, t).getIZ();
        int size = p.length();
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < t.length() - size + 1; i++)
            if (z[i] + iz[i + size - 1] > size - 2)
                list.add(i + 1);
        out.println(list.size());
        for (Integer n : list)
            out.print(n + " ");
        out.close();
    }

    static class ZFunction {
        private String main;
        private int[] z;
        private int size;

        ZFunction(String p, String t) {
            size = t.length();
            this.main = p + "#" + t;
            z = new int[main.length()];
            int left = 0;
            int right = 0;
            for (int i = 1; i < main.length(); i++)
                if (i > right) {
                    int j = 0;
                    while (i + j < main.length() && main.charAt(i + j) == main.charAt(j))
                        j++;
                    z[i] = j;
                    left = i;
                    right = i + j - 1;
                } else if (z[i - left] < right - i + 1)
                    z[i] = z[i - left];
                else {
                    int j = 1;
                    while (j + right < main.length() && main.charAt(j + right - i) == main.charAt(right + j))
                        j++;
                    z[i] = right + j - i;
                    left = i;
                    right = right + j - 1;
                }
        }

        private int[] getZ() {
            int res[] = new int[size];
            for (int i = 0; i < size; i++)
                res[size - 1 - i] = z[z.length - 1 - i];
            return res;
        }
    }

    static class IZFunction {
        private String main;
        private int[] z;
        private int size;

        IZFunction(String p, String t) {
            size = t.length();
            char[] pp = new char[p.length()];
            for (int i = 1; i <= p.length(); i++)
                pp[p.length() - i] = p.charAt(i - 1);
            char[] tt = new char[t.length()];
            for (int i = 1; i <= t.length(); i++)
                tt[t.length() - i] = t.charAt(i - 1);
            p = new String(pp);
            t = new String(tt);
            this.main = p + "#" + t;
            z = new int[main.length()];
            int left = 0;
            int right = 0;
            for (int i = 1; i < main.length(); i++)
                if (i > right) {
                    int j = 0;
                    while (i + j < main.length() && main.charAt(i + j) == main.charAt(j))
                        j++;
                    z[i] = j;
                    left = i;
                    right = i + j - 1;
                } else if (z[i - left] < right - i + 1)
                    z[i] = z[i - left];
                else {
                    int j = 1;
                    while (j + right < main.length() && main.charAt(j + right - i) == main.charAt(right + j))
                        j++;
                    z[i] = right + j - i;
                    left = i;
                    right = right + j - 1;
                }
        }

        private int[] getIZ() {
            int res[] = new int[size];
            for (int i = 0; i < size; i++)
                res[i] = z[z.length - 1 - i];
            return res;
        }
    }

}
