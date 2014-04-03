import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class C {
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("prefix.in"));
        PrintWriter out = new PrintWriter("prefix.out");
        String p = in.readLine();
        PrefixFunction pf = new PrefixFunction(p);
        pf.printPF(out);
        out.close();
    }

    static class PrefixFunction {
        private String main;
        private int[] pf;

        PrefixFunction(String p) {
            this.main = p;
            pf = new int[main.length()];
            pf[0] = 0;
            for (int i = 1; i < main.length(); i++) {
                int k = pf[i - 1];
                while (k > 0 && main.charAt(i) != main.charAt(k))
                    k = pf[k - 1];
                if (main.charAt(i) == main.charAt(k))
                    k++;
                pf[i] = k;
            }
        }

        private void printPF(PrintWriter out) {
            for (Integer n : pf)
                out.print(n + " ");
        }
    }
}