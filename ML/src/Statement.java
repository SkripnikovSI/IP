import java.util.ArrayList;
import java.util.HashMap;

class Statement {
    Statement l;
    Statement r;
    final Type type;
    String value;
    int code;

    Statement(Statement l, Statement r, Type type, String value) {
        this.l = l;
        this.r = r;
        this.type = type;
        if (type == Type.INVERSION) {
            if (r.type.getPriority() < type.getPriority())
                this.value = "!(" + r.value + ")";
            else
                this.value = "!" + r.value;
        } else if (type == Type.VARIABLE) {
            this.value = value;
        } else {
            String lValue = l.value;
            String rValue = r.value;
            if (l.type.getPriority() <= type.getPriority())
                lValue = "(" + l.value + ")";
            if (r.type.getPriority() <= type.getPriority())
                rValue = "(" + r.value + ")";
            this.value = lValue + type.getSign() + rValue;
        }
        code = this.value.hashCode();
    }

    public void deleteValue() {
        value = null;
        if (l != null && l.type != Type.VARIABLE)
            l.deleteValue();
        if (r != null && r.type != Type.VARIABLE)
            r.deleteValue();
    }

    public String[] getVariable() {
        ArrayList<String> list = new ArrayList<String>();
        addVariable(list);
        return list.toArray(new String[list.size()]);
    }

    private void addVariable(ArrayList<String> list) {
        if (type == Type.VARIABLE) {
            if (!list.contains(value))
                list.add(value);
        } else if (type == Type.INVERSION) {
            r.addVariable(list);
        } else {
            l.addVariable(list);
            r.addVariable(list);
        }
    }

    public Statement substituteStatements(Statement[] statements, HashMap<String, Statement> map) {
        if (type == Type.VARIABLE) {
            Statement s;
            if (map.containsKey(value)) {
                s = map.get(value);
            } else {
                s = statements[map.size()];
                map.put(value, s);
            }
            return s;
        } else if (type == Type.INVERSION) {
            return new Statement(null, r.substituteStatements(statements, map), type, null);
        } else {
            return new Statement(l.substituteStatements(statements, map), r.substituteStatements(statements, map), type, null);
        }
    }

    public boolean hasFormOfThisStatement(Statement s) {
        return hasFormOfThisStatementR(s, new HashMap<String, Statement>());
    }

    public boolean hasFormOfThisStatementR(Statement s, HashMap<String, Statement> map) {
        if (s.type == Type.VARIABLE) {
            if (map.containsKey(s.value)) {
                return equ(map.get(s.value));
            } else {
                map.put(s.value, this);
                return true;
            }
        } else if (type == s.type) {
            if (type == Type.INVERSION) {
                return r.hasFormOfThisStatementR(s.r, map);
            } else {
                return l.hasFormOfThisStatementR(s.l, map) && r.hasFormOfThisStatementR(s.r, map);
            }
        } else
            return false;
    }

    public boolean equ(Statement s) {
        if (code == s.code && type == s.type) {
            if (r == null) {
                return value.equals(s.value);
            } else if (l == null) {
                return r.equ(s.r);
            } else {
                return r.equ(s.r) && l.equ(s.l);
            }
        }
        return false;
    }

    enum Type {
        VARIABLE {
            public int getPriority() {
                return 10;
            }

            public String getSign() {
                return "";
            }
        },
        INVERSION {
            public int getPriority() {
                return 8;
            }

            public String getSign() {
                return "!";
            }
        },
        CONJUNCTION {
            public int getPriority() {
                return 6;
            }

            public String getSign() {
                return "&";
            }
        },
        DISJUNCTION {
            public int getPriority() {
                return 4;
            }

            public String getSign() {
                return "|";
            }
        },
        IMPLICATION {
            public int getPriority() {
                return 2;
            }

            public String getSign() {
                return "->";
            }
        };

        public abstract int getPriority();

        public abstract String getSign();
    }
}
