import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Predicate extends Statement {
    ArrayList<Term> list;

    Predicate(String name, ArrayList<Term> list) {
        this.list = list;
        if (list == null) {
            value = name;
        } else {
            String value = list.get(0).value;
            for (int i = 1; i < list.size(); i++)
                value += "," + list.get(i).value;
            this.value = name + "(" + value + ")";
        }
        code = value.hashCode();
    }

    protected boolean soClose(Statement s, HashSet<String> couplingVariables) {
        if (getClass() == s.getClass()) {
            Predicate p = (Predicate) s;
            if (list != null && p.list != null && list.size() == p.list.size()) {
                for (int i = 0; i < list.size(); i++) {
                    if (!list.get(i).soClose(p.list.get(i), couplingVariables))
                        return false;
                }
                return true;
            } else if (value.equals(s.value)) {
                return true;
            }
        }
        return false;
    }

    protected void addFreeVariables(HashSet<String> freeVariables, HashSet<String> couplingVariables) {
        if (list != null) {
            for (Term aList : list) aList.addFreeVariables(freeVariables, couplingVariables);
        }
    }

    public Statement substituteStatements(Statement[] statements, HashMap<String, Statement> map) {
        if(list == null) {
            Statement s;
            if (map.containsKey(value)) {
                s = map.get(value);
            } else {
                s = statements[map.size()];
                map.put(value, s);
            }
            return s;
        } else {
            return this;
        }
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

    protected String getSign() {
        return value;
    }
}

