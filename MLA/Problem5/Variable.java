import java.util.HashSet;

public class Variable extends Term {
    Variable(String name) {
        this.name = name;
        value = name;
        code = value.hashCode();
    }

    public boolean soClose(Term t, HashSet<String> couplingVariables) {
        if (e(mainVariable) && !couplingVariables.contains(value) && !couplingVariables.contains(t.value)) {
            if (substitutionVariable == null) {
                substitutionVariable = t.value;
                return true;
            } else {
                return t.value.equals(substitutionVariable);
            }
        } else
            return e(t);
    }

    public void addFreeVariables(HashSet<String> freeVariables, HashSet<String> couplingVariables) {
        if (!couplingVariables.contains(name))
            freeVariables.add(name);
    }
}
