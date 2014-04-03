import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class A {
    static ArrayList<Statement> axioms = new ArrayList<Statement>();

    public static void main(String[] args) throws ParseException, IOException {
        BufferedReader in = new BufferedReader(new FileReader(args[0]));
        ArrayList<Statement> list = new ArrayList<Statement>();

        intiA();
        long time = System.currentTimeMillis();
        boolean success = true;
        String line = in.readLine();
        int k = 0;
        while (line != null) {
            Statement s = Parser.parse(line);
            s.deleteValue();

            System.out.println(k++);

            if (checkingEquivalenceAxioms(s)) {
                list.add(s);
                line = in.readLine();
                continue;
            }
            success = false;
            m:
            for (int j = 0; j < list.size(); j++)
                if (list.get(j).type == Statement.Type.IMPLICATION && list.get(j).r.equ(s))
                    for (Statement aList : list)
                        if (list.get(j).l.equ(aList)) {
                            list.get(j).l = aList;
                            list.get(j).r = s;
                            success = true;
                            break m;
                        }
            if (!success) {
                System.out.println("Доказательство некорректно начиная с высказывания номер " + list.size());
                break;
            }

            list.add(s);
            line = in.readLine();
        }

        System.out.println(System.currentTimeMillis() - time);


        if (success)
            System.out.println("Доказательство корректно");


        System.out.println(System.currentTimeMillis() - time);
    }

    public static boolean checkingEquivalenceAxioms(Statement s) {
        for (Statement axiom : axioms) {
            if (s.hasFormOfThisStatement(axiom))
                return true;
        }
        return false;
        /*
        return s.type == Statement.Type.IMPLICATION && (s.r.type == Statement.Type.IMPLICATION && (s.l.equ(s.r.r) || // сх. ак. 1
                s.r.r.type == Statement.Type.CONJUNCTION &&
                        s.l.equ(s.r.r.l) && s.r.l.equ(s.r.r.r) || // сх. ак. 3
                s.r.r.type == Statement.Type.INVERSION &&
                        s.l.type == Statement.Type.IMPLICATION &&
                        s.r.l.type == Statement.Type.IMPLICATION && s.r.l.r.type == Statement.Type.INVERSION &&
                        s.l.l.equ(s.r.l.l) && s.l.l.equ(s.r.r.r) && s.l.r.equ(s.r.l.r.r) || // сх. ак. 9
                s.r.r.type == Statement.Type.IMPLICATION && (
                        s.l.type == Statement.Type.IMPLICATION && s.r.l.type == Statement.Type.IMPLICATION &&
                                s.r.l.r.type == Statement.Type.IMPLICATION && s.l.l.equ(s.r.l.l) && s.l.l.equ(s.r.r.l) &&
                                s.l.r.equ(s.r.l.r.l) && s.r.l.r.r.equ(s.r.r.r) || //сх. ак. 2
                                s.l.type == Statement.Type.IMPLICATION && s.r.l.type == Statement.Type.IMPLICATION &&
                                        s.r.r.l.type == Statement.Type.DISJUNCTION && s.l.l.equ(s.r.r.l.l) &&
                                        s.l.r.equ(s.r.l.r) && s.l.r.equ(s.r.r.r) && s.r.l.l.equ(s.r.r.l.r))) || // сх. ак. 8
                s.r.type == Statement.Type.DISJUNCTION && (s.l.equ(s.r.l) || s.l.equ(s.r.r)) || // сх. ак. 6 или 7
                s.l.type == Statement.Type.CONJUNCTION && (s.r.equ(s.l.l) || s.r.equ(s.l.r)) || // сх. ак. 4 или 5
                s.l.type == Statement.Type.INVERSION && s.l.r.type == Statement.Type.INVERSION && s.l.r.r.equ(s.r));// сх. ак. 10
         */
    }

    private static void intiA() throws ParseException {
        axioms.add(Parser.parse("A->(B->A)"));
        axioms.add(Parser.parse("(A->B)->(A->B->C)->(A->C)"));
        axioms.add(Parser.parse("A->B->A&B"));
        axioms.add(Parser.parse("A&B->A"));
        axioms.add(Parser.parse("A&B->B"));
        axioms.add(Parser.parse("A->A|B"));
        axioms.add(Parser.parse("B->A|B"));
        axioms.add(Parser.parse("(A->B)->(C->B)->(A|C->B)"));
        axioms.add(Parser.parse("(A->B)->(A->!B)->!A"));
        axioms.add(Parser.parse("!!A->A"));
    }
}
