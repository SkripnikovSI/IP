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

    public boolean isInduction() {
        if (l instanceof Conjunction) {
            Conjunction c = (Conjunction) l;
            if (c.r instanceof UniversalQuantifier) {
                UniversalQuantifier uq = (UniversalQuantifier) c.r;
                if (uq.s instanceof Implication) {
                    Implication i = (Implication) uq.s;
                    Term.mainVariable = uq.v;
                    Term.substitutionVariable = "0";
                    boolean result = i.l.e(r) && r.soClose(c.l, new HashSet<String>());
                    Term.substitutionVariable = Term.mainVariable.value + "'";
                    if (result)
                        result = result && r.soClose(i.r, new HashSet<String>());
                    Term.mainVariable = null;
                    Term.substitutionVariable = null;
                    return result;
                }
            }
        }
        return false;
    }

    private boolean isEleventhOrTwelfthAxiom(Quantifier q, Statement s) {
        Term.mainVariable = q.v;
        Term.substitutionVariable = null;
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
