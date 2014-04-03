import java.util.HashMap;

public abstract class Unary extends Statement {
    Statement s;

    protected Unary(Statement s, String sing) {
        this.s = s;
        if (s.getPriority() < getPriority())
            value = getSign() + sing + "(" + s.value + ")";
        else
            value = getSign() + sing + s.value;
        code = value.hashCode();
    }

    public boolean hasFormOfThisStatementR(Statement s, HashMap<String, Statement> map) {
        return super.hasFormOfThisStatementR(s, map) || s.getClass() == getClass() &&
                this.s.hasFormOfThisStatementR(((Unary) s).s, map);
    }

    public boolean isEleventhAxiom() {
        return false;
    }

    public boolean isTwelfthAxiom() {
        return false;
    }

    protected int getPriority() {
        return 8;
    }
}
