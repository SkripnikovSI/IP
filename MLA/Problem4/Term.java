import java.util.HashSet;

public abstract class Term {
    static Variable mainVariable = null;
    static Variable substitutionVariable = null;
    String name;
    String value;
    int code;

    public abstract boolean soClose(Term t, HashSet<String> couplingVariables);

    public abstract void addFreeVariables(HashSet<String> freeVariables, HashSet<String> couplingVariables);

    public boolean e(Term t) {
        return code == t.code && value.equals(t.value);
    }
}
