import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class B {
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("search2.in"));
        PrintWriter out = new PrintWriter("search2.out");
        String p = in.readLine();
        String t = in.readLine();
        PrefixFunction pf = new PrefixFunction(p, t);
        pf.printListSize(out);
        pf.printList(out);
        out.close();
    }

    static class PrefixFunction {
        private String main;
        private int[] pf;
        private int pSize;
        private ArrayList<Integer> list = new ArrayList<Integer>();

        PrefixFunction(String p, String t) {
            this.main = p + "#" + t;
            pSize = p.length();
            pf = new int[main.length()];
            pf[0] = 0;
            for (int i = 1; i < main.length(); i++) {
                int k = pf[i - 1];
                while (k > 0 && main.charAt(i) != main.charAt(k))
                    k = pf[k - 1];
                if (main.charAt(i) == main.charAt(k))
                    k++;
                pf[i] = k;
                if (k == pSize)
                    list.add(i - 2 * pSize + 1);
            }
        }

        private void printListSize(PrintWriter out) {
            out.println(list.size());
        }

        private void printList(PrintWriter out) {
            for (Integer n : list)
                out.print(n + " ");
        }
    }
}
