import java.util.HashSet;

public abstract class Quantifier extends Unary {
    Variable v;

    Quantifier(Statement s, Variable v) {
        super(s, v.name);
        this.v = v;
    }

    protected void addFreeVariables(HashSet<String> freeVariables, HashSet<String> couplingVariables) {
        if (couplingVariables.contains(v.name)) {
            s.addFreeVariables(freeVariables, couplingVariables);
        } else {
            couplingVariables.add(v.name);
            s.addFreeVariables(freeVariables, couplingVariables);
            couplingVariables.remove(v.name);
        }
    }
}
