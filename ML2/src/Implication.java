import java.util.HashMap;
import java.util.HashSet;

public class Implication extends Binary {
    Implication(Statement l, Statement r) {
        super(l, r);
    }

    public Statement substituteStatements(Statement[] statements, HashMap<String, Statement> map) {
        return new Implication(l.substituteStatements(statements, map), r.substituteStatements(statements, map));
    }

    public boolean isEleventhAxiom() {
        return l instanceof UniversalQuantifier && isEleventhOrTwelfthAxiom((Quantifier) l, r);
    }

    public boolean isTwelfthAxiom() {
        return r instanceof ExistentialQuantifier && isEleventhOrTwelfthAxiom((Quantifier) r, l);
    }

    private boolean isEleventhOrTwelfthAxiom(Quantifier q, Statement s) {
        Term.mainVariable = q.v;
        boolean result = q.s.soClose(s, new HashSet<String>());
        Term.mainVariable = null;
        Term.substitutionVariable = null;
        return result;
    }

    protected int getPriority() {
        return 2;
    }

    protected String getSign() {
        return "->";
    }
}
