import java.util.HashMap;

public class Disjunction extends Binary {
    Disjunction(Statement l, Statement r) {
        super(l, r);
    }

    public Statement substituteStatements(Statement[] statements, HashMap<String, Statement> map) {
        return new Disjunction(l.substituteStatements(statements, map), r.substituteStatements(statements, map));
    }

    public boolean isEleventhAxiom() {
        return false;
    }

    public boolean isTwelfthAxiom() {
        return false;
    }

    protected int getPriority() {
        return 4;
    }

    protected String getSign() {
        return "|";
    }

}
