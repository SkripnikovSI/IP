import java.util.Arrays;

public class M implements Recursion {
    private Recursion f;

    M(Recursion f) {
        this.f = f;
    }

    public long applyTo(long[] x) {
        x = Arrays.copyOf(x, x.length + 1);
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            x[x.length - 1] = i;
            if (f.applyTo(x) == 0)
                return i;
        }
        return 0;
    }
}
