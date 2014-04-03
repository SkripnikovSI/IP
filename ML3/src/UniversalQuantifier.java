import java.util.HashMap;
import java.util.HashSet;

public class UniversalQuantifier extends Quantifier {

    UniversalQuantifier(Variable v, Statement s) {
        super(s, v);
    }

    protected boolean soClose(Statement s, HashSet<String> couplingVariables) {
        if (s.getClass() == getClass() && v.e(((UniversalQuantifier) s).v)) {
            if (couplingVariables.contains(v.name)) {
                return this.s.soClose(((UniversalQuantifier) s).s, couplingVariables);
            } else {
                couplingVariables.add(v.name);
                boolean b = this.s.soClose(((UniversalQuantifier) s).s, couplingVariables);
                couplingVariables.remove(v.name);
                return b;
            }
        }
        return false;
    }

    public Statement substituteStatements(Statement[] statements, HashMap<String, Statement> map) {
        return new UniversalQuantifier(v, s.substituteStatements(statements, map));
    }

    protected String getSign() {
        return "@";
    }
}
