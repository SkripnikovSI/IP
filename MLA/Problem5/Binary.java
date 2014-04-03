import java.util.HashMap;
import java.util.HashSet;

public abstract class Binary extends Statement {
    Statement l;
    Statement r;

    Binary(Statement l, Statement r) {
        this.l = l;
        this.r = r;
        String lValue = l.value;
        String rValue = r.value;
        if (l.getPriority() <= getPriority())
            lValue = "(" + l.value + ")";
        if (r.getPriority() <= getPriority())
            rValue = "(" + r.value + ")";
        this.value = lValue + getSign() + rValue;
        code = value.hashCode();
    }

    protected boolean hasFormOfThisStatementR(Statement s, HashMap<String, Statement> map) {
        return super.hasFormOfThisStatementR(s, map) || s.getClass() == getClass() &&
                l.hasFormOfThisStatementR(((Binary) s).l, map) &&
                r.hasFormOfThisStatementR(((Binary) s).r, map);
    }

    protected boolean soClose(Statement s, HashSet<String> couplingVariables) {
        return s.getClass() == getClass() &&
                l.soClose(((Binary) s).l, couplingVariables) &&
                r.soClose(((Binary) s).r, couplingVariables);
    }

    protected void addFreeVariables(HashSet<String> freeVariables, HashSet<String> couplingVariables) {
        l.addFreeVariables(freeVariables, couplingVariables);
        r.addFreeVariables(freeVariables, couplingVariables);
    }
}
