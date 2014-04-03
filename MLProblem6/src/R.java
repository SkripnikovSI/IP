import java.util.Arrays;

public class R implements Recursion {
    private Recursion f;
    private Recursion g;

    R(Recursion f, Recursion g) {
        this.f = f;
        this.g = g;
    }

    public long applyTo(long[] x) {
        x = Arrays.copyOf(x, x.length);
        long y = x[x.length - 1];
        if(y == 0) {
            return f.applyTo(Arrays.copyOf(x, x.length - 1));
        } else {
            long[] x2 = Arrays.copyOf(x, x.length + 1);
            x2[x2.length - 2] = y - 1;
            x[x.length - 1] = y - 1;
            x2[x2.length - 1] = applyTo(x);
            return g.applyTo(x2);
        }
    }
}
