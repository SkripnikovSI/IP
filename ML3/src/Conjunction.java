import java.util.HashMap;

public class Conjunction extends Binary {
    Conjunction(Statement l, Statement r) {
        super(l, r);
    }

    public Statement substituteStatements(Statement[] statements, HashMap<String, Statement> map) {
        return new Conjunction(l.substituteStatements(statements, map), r.substituteStatements(statements, map));
    }

    public boolean isEleventhAxiom() {
        return false;
    }

    public boolean isTwelfthAxiom() {
        return false;
    }

    public boolean isInduction() {
        return false;
    }

    protected int getPriority() {
        return 6;
    }

    protected String getSign() {
        return "&";
    }
}
