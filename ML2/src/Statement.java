import java.util.HashMap;
import java.util.HashSet;

abstract class Statement {
    String value;
    int code;

    public boolean e(Statement s) {
        return code == s.code && value.equals(s.value);
    }

    public boolean hasFormOfThisStatement(Statement s) {
        return hasFormOfThisStatementR(s, new HashMap<String, Statement>());
    }

    protected boolean hasFormOfThisStatementR(Statement s, HashMap<String, Statement> map) {
        if (s instanceof Predicate) {
            if (map.containsKey(s.value)) {
                return e(map.get(s.value));
            } else {
                map.put(s.value, this);
                return true;
            }
        } else
            return false;
    }

    public abstract boolean isEleventhAxiom();

    public abstract boolean isTwelfthAxiom();

    protected abstract boolean soClose(Statement s, HashSet<String> couplingVariables);

    public HashSet<String> getFreeVariables() {
        HashSet<String> freeVariables = new HashSet<String>();
        addFreeVariables(freeVariables, new HashSet<String>());
        return freeVariables;
    }

    protected abstract void addFreeVariables(HashSet<String> freeVariables, HashSet<String> couplingVariables);

    public abstract Statement substituteStatements(Statement[] statements, HashMap<String, Statement> map);

    protected abstract int getPriority();

    protected abstract String getSign();
}
