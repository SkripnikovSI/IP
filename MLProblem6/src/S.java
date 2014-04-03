public class S implements Recursion {
    private Recursion f;
    private Recursion[] g;

    S(Recursion f, Recursion[] g) {
        this.f = f;
        this.g = g;
    }

    public long applyTo(long[] x) {
        long[] y = new long[g.length];
        for (int i = 0; i < y.length; i++)
            y[i] = g[i].applyTo(x);
        return f.applyTo(y);
    }
}
