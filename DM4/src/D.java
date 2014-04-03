import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Stack;

public class D {

    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(new FileReader("euler.in"));
        PrintWriter out = new PrintWriter("euler.out");
        int n = in.nextInt() + 1;
        int g[][] = new int[n][n];
        Stack<Integer> stack = new Stack<Integer>();
        Stack<Integer> answer = new Stack<Integer>();
        int q = 0;
        int v = 1;
        for (int i = 1; i < n; i++) {
            int m = in.nextInt() + 1;
            for (int j = 1; j < m; j++) {
                g[i][in.nextInt()]++;
                g[i][0]++;
            }
            if (g[i][0] % 2 == 1) {
                q++;
                v = i;
            }
        }
        if (q == 0 || q == 2) {
            stack.push(v);
            while (!stack.isEmpty()) {
                int u = stack.peek();
                if (g[u][0] > 0)
                    for (int i = 1; i < n; i++)
                        if (g[u][i] != 0) {
                            stack.push(i);
                            g[u][i]--;
                            g[i][u]--;
                            g[u][0]--;
                            g[i][0]--;
                            break;
                        }
                if (u == stack.peek())
                    answer.push(stack.pop());
            }
            out.println(answer.size() - 1);
            while (!answer.isEmpty())
                out.print(answer.pop() + " ");
        } else {
            out.println(-1);
        }
        out.close();
    }
}
