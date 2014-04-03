import java.util.HashMap;
import java.util.HashSet;

public class ExistentialQuantifier extends Quantifier {

    ExistentialQuantifier(Variable v, Statement s) {
        super(s, v);
    }

    protected boolean soClose(Statement s, HashSet<String> couplingVariables) {
        if (s.getClass() == getClass() && v.e(((ExistentialQuantifier) s).v)) {
            if (couplingVariables.contains(v.name)) {
                return this.s.soClose(((ExistentialQuantifier) s).s, couplingVariables);
            } else {
                couplingVariables.add(v.name);
                boolean b = this.s.soClose(((ExistentialQuantifier) s).s, couplingVariables);
                couplingVariables.remove(v.name);
                return b;
            }
        }
        return false;
    }

    public Statement substituteStatements(Statement[] statements, HashMap<String, Statement> map) {
        return new ExistentialQuantifier(v, s.substituteStatements(statements, map));
    }

    protected String getSign() {
        return "?";
    }
}
