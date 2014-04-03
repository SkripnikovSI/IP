import java.util.ArrayList;
import java.util.HashSet;

public class Function extends Term {
    private ArrayList<Term> list;

    Function(String name, ArrayList<Term> list) {
        this.name = name;
        this.list = list;
        if ("+".equals(name)) {
            value = list.get(0).value + "+" + list.get(1).value;
        } else if ("*".equals(name)) {
            value = list.get(0).value;
            if (list.get(0).name.equals("+")) {
                value = "(" + value + ")";
            }
            if (list.get(1).name.equals("+")) {
                value = "*(" + list.get(1).value + ")";
            } else {
                value = "*" + list.get(1).value;
            }
        } else if ("'".equals(name)) {
            if (!"*".equals(list.get(0).name) && !"+".equals(list.get(0).name)) {
                value = list.get(0).value + "'";
            } else {
                value = "(" + list.get(0).value + ")'";
            }
        } else if ("0".equals(name)) {
            value = name;
        } else {
            value = list.get(0).value;
            for (int i = 1; i < list.size(); i++)
                value += "," + list.get(i).value;
            value = name + "(" + value + ")";
        }
        code = value.hashCode();
    }

    public boolean soClose(Term t, HashSet<String> couplingVariables) {
        if (getClass() == t.getClass()) {
            Function f = (Function) t;
            if (name.equals(f.name)) {
                if (name.equals("0"))
                    return true;
                if (list.size() == f.list.size()) {
                    for (int i = 0; i < list.size(); i++) {
                        if (!list.get(i).soClose(f.list.get(i), couplingVariables))
                            return false;
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public void addFreeVariables(HashSet<String> freeVariables, HashSet<String> couplingVariables) {
        if (list != null)
            for (Term aList : list) aList.addFreeVariables(freeVariables, couplingVariables);
    }
}
