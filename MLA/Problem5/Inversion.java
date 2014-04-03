import java.util.HashMap;
import java.util.HashSet;

public class Inversion extends Unary {
    Inversion(Statement s) {
        super(s, "");
    }

    protected boolean soClose(Statement s, HashSet<String> couplingVariables) {
        return s.getClass() == getClass() && this.s.soClose(((Inversion) s).s, couplingVariables);
    }

    protected void addFreeVariables(HashSet<String> freeVariables, HashSet<String> couplingVariables) {
        s.addFreeVariables(freeVariables, couplingVariables);
    }

    public Statement substituteStatements(Statement[] statements, HashMap<String, Statement> map) {
        return new Inversion(s.substituteStatements(statements, map));
    }

    protected String getSign() {
        return "!";
    }
}
