public class Main {
    private static Recursion Z = new Z();
    private static Recursion N = new N();
    private static Recursion sum = R(U(1), S(N, U(3)));
    /*private static Recursion sum = new Recursion() {
        @Override
        public int applyTo(int... x) {
            return x[0] + x[1];
        }
    };  */
    private static Recursion dec = R(Z, U(1));
    //private static Recursion sub = R(U(1), S(dec, U(3)));
    private static Recursion sub = new Recursion() {
        @Override
        public long applyTo(long... x) {
            long y = x[0] - x[1];
            if (y < 0)
                return 0;
            return y;
        }
    };
    //private static Recursion mul = R(Z, S(sum, U(1), U(3)));
    private static Recursion mul = new Recursion() {
        @Override
        public long applyTo(long... x) {
            return x[0] * x[1];
        }
    };
    private static Recursion isZero = R(S(N, Z), Z); //true - 1; false - 0;
    private static Recursion INV = isZero;
    private static Recursion lessOrEqual = S(isZero, sub);
    private static Recursion greaterOrEqual = S(lessOrEqual, U(2), U(1));
    private static Recursion less = S(mul, lessOrEqual, S(INV, greaterOrEqual));
    private static Recursion equal = S(mul, lessOrEqual, greaterOrEqual);
    private static Recursion IF = R(U(2), U(1)); // x, y, bool: true - first; false - second
    private static Recursion OR = S(IF, U(1), U(2), U(1));// S(INV, S(isZero, sum));
    // private static Recursion maxdiv = R(Z, S(IF, S(N, U(2)), U(3), S(equal, S(sub, S(N, U(2)), U(3)), U(1))));
    // private static Recursion div = S(R(Z, S(sum, U(3), S(equal, S(N, U(2)), S(maxdiv, U(1), S(N, U(2)))))), U(2), U(1));
    private static Recursion div = new Recursion() {
        @Override
        public long applyTo(long... x) {
            return x[0] / x[1];
        }
    };
    private static Recursion mod = S(sub, U(1), S(mul, U(2), S(div, U(1), U(2))));
    /*private static Recursion mod = new Recursion() {
         @Override
         public int applyTo(int... x) {
             return x[0] % x[1];
         }
     };    */
    private static Recursion isPrime = S(INV, S(R(Z, S(OR, S(isZero, S(mod, U(1), S(N, S(N, U(2))))), U(3))), U(1), S(dec, S(dec, S(dec, U(1))))));
    private static Recursion nextPrime = M(S(INV, S(mul, less, S(isPrime, U(2)))));
    private static Recursion nthPrime = R(S(N, S(N, Z)), S(nextPrime, U(2)));
    private static Recursion pow = R(S(N, Z), S(mul, U(1), U(3)));
    //private static Recursion plog = S(dec, M(S(INV, S(mod, U(2), S(pow, U(1), U(3))))));
    private static Recursion plog = new Recursion() {
        @Override
        public long applyTo(long... x) {
            //System.out.println(x[0] +" "+ x[1]);
            for (int i = 0; ; i++)
                if (x[1] % Math.pow(x[0], i) != 0)  {
                    //System.out.println(i - 1);
                    return i - 1;
                }
        }
    };
    private static Recursion one = S(N, Z);
    private static Recursion two = S(N, one);

    //stack: WOMN*first^M*second^N | WOMN*first^?*second^?*third^?
    private static Recursion stackSize = S(plog, two, U(1));
    private static Recursion first = S(nthPrime, S(dec, stackSize));
    private static Recursion second = S(nthPrime, stackSize);
    private static Recursion third = S(nthPrime, S(N, stackSize));
    private static Recursion getM = S(plog, first, U(1));
    private static Recursion getN = S(plog, second, U(1));
    private static Recursion getWOMN = S(div, U(1), S(mul, two, S(mul, S(pow, first, getM), S(pow, second, getN))));
    //evil: first^(n-1) | first^(m-1)*second | first(m-1)*second^m*third^(n-1)
    private static Recursion evil = S(IF, S(IF, S(mul, S(pow, two, two), S(mul, S(mul, S(pow, first, S(dec, getM)), S(pow, second, getM)), S(pow, third, S(dec, getN)))), S(mul, two, S(mul, S(pow, first, S(dec, getM)), second)), getN), S(pow, first, S(N, getN)), getM);
    private static Recursion nextStack = S(mul, getWOMN, evil);
    private static Recursion AckOneOne = S(R(U(1), S(nextStack, U(3))), U(1), M(S(dec, S(stackSize, R(U(1), S(nextStack, U(3)))))));
    private static Recursion Ackerman = S(plog, S(nthPrime, one), S(AckOneOne, S(mul, S(pow, two, two), S(mul, S(pow, S(nthPrime, one), U(1)), S(pow, S(nthPrime, two), U(2))))));

    public static void main(String[] args) {
        testAckerman(3, 1);
    }

    public static Recursion U(int i) {
        return new U(i);
    }

    public static Recursion S(Recursion... f) {
        Recursion[] g = new Recursion[f.length - 1];
        for (int i = 0; i < g.length; i++)
            g[i] = f[i + 1];
        return new S(f[0], g);
    }

    public static Recursion R(Recursion f, Recursion g) {
        return new R(f, g);
    }

    public static Recursion M(Recursion f) {
        return new M(f);
    }

    public static void testAckerman(int a, int b) {
        System.out.println("Ackerman " + a + " " + b + ": " + Ackerman.applyTo(a, b));
    }
}
