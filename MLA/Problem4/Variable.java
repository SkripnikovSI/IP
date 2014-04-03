import java.util.HashSet;

public class Variable extends Term {
    Variable(String name) {
        this.name = name;
        value = name;
        code = value.hashCode();
    }

    public boolean soClose(Term t, HashSet<String> couplingVariables) {
        if (getClass() == t.getClass()) {
            Variable v = (Variable) t;
            if (e(mainVariable) && !couplingVariables.contains(value) && !couplingVariables.contains(v.value)) {
                if (substitutionVariable == null) {
                    substitutionVariable = v;
                    return true;
                } else {
                    return v.e(substitutionVariable);
                }
            } else
                return e(t);
        }
        return false;
    }

    public void addFreeVariables(HashSet<String> freeVariables, HashSet<String> couplingVariables) {
        if (!couplingVariables.contains(name))
            freeVariables.add(name);
    }
}
