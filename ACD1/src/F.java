import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class F {
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("period.in"));
        PrintWriter out = new PrintWriter("period.out");
        String p = in.readLine();
        ZFunction zf = new ZFunction(p);
        int z[] = zf.getZ();
        int t = p.length();
        for (int i = 1; i < p.length(); i++) {
            if (z[i] == p.length() - i && p.length() % i == 0) {
                t = i;
                break;
            }
        }
        out.println(t);
        out.close();
    }

    static class ZFunction {
        private String main;
        private int[] z;

        ZFunction(String p) {
            this.main = p;
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
            return z;
        }
    }
}