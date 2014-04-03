import java.util.ArrayList;
import java.util.HashSet;

public class Function extends Term {
    ArrayList<Term> list;

    Function(String name, ArrayList<Term> list) {
        this.name = name;
        this.list = list;
        String value = list.get(0).value;
        for (int i = 1; i < list.size(); i++)
            value += "," + list.get(i).value;
        this.value = name + "(" + value + ")";
        code = value.hashCode();
    }

    public boolean soClose(Term t, HashSet<String> couplingVariables) {
        if (getClass() == t.getClass()) {
            Function f = (Function) t;
            if (list.size() == f.list.size()) {
                for (int i = 0; i < list.size(); i++) {
                    if (!list.get(i).soClose(f.list.get(i), couplingVariables))
                        return false;
                }
                return true;
            }
        }
        return false;
    }

    public void addFreeVariables(HashSet<String> freeVariables, HashSet<String> couplingVariables) {
        for (Term aList : list) aList.addFreeVariables(freeVariables, couplingVariables);
    }
}
