public class U implements Recursion {
    private int i;

    U(int i) {
        this.i = i;
    }

    public long applyTo(long[] x) {
        return x[i - 1];
    }
}
