import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class A {
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("search1.in"));
        PrintWriter out = new PrintWriter("search1.out");
        String p = in.readLine();
        String t = in.readLine();
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 1; i < t.length() - p.length() + 2; i++)
            if (isSubstring(p, t, i))
                list.add(i);
        out.println(list.size());
        for (Integer n : list)
            out.print(n + " ");
        out.close();
    }

    private static boolean isSubstring(String p, String t, int number) {
        for (int i = 0; i < p.length(); i++) {
            if (p.charAt(i) != t.charAt(i + number - 1))
                return false;
        }
        return true;
    }
}
