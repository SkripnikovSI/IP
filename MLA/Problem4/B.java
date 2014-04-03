import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class B {
    static ArrayList<Statement> axioms = new ArrayList<Statement>();

    public static void main(String[] args) throws ParseException, IOException {
        BufferedReader in = new BufferedReader(new FileReader(args[0]));
        ArrayList<Statement> list = new ArrayList<Statement>();
        intiAxioms();

        long time = System.currentTimeMillis();

        String line = in.readLine();

        boolean success = true;

        while (line != null) {
            Statement s = Parser.parse(line);

            if (isEquivalentToAxiom(s)) {
                list.add(s);
                line = in.readLine();
                continue;
            }
            success = false;
            if (s instanceof Implication) {
                Implication im = (Implication) s;
                if (im.r instanceof UniversalQuantifier) {
                    UniversalQuantifier uq = (UniversalQuantifier) im.r;
                    if (!im.l.getFreeVariables().contains(uq.v.name)) {
                        for (Statement aList : list) {
                            if (aList instanceof Implication) {
                                Implication im2 = (Implication) aList;
                                if (im2.l.e(im.l) && im2.r.e(uq.s)) {
                                    im.l = im2.l;
                                    uq.s = im2.r;
                                    success = true;
                                    break;
                                }
                            }
                        }
                    }
                }
                if (im.l instanceof ExistentialQuantifier) {
                    ExistentialQuantifier eq = (ExistentialQuantifier) im.l;
                    if (!im.r.getFreeVariables().contains(eq.v.name)) {
                        for (Statement aList : list) {
                            if (aList instanceof Implication) {
                                Implication im2 = (Implication) aList;
                                if (im2.r.e(im.r) && im2.l.e(eq.s)) {
                                    im.r = im2.r;
                                    eq.s = im2.l;
                                    success = true;
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            if (!success) {
                m:
                for (int j = 0; j < list.size(); j++)
                    if (list.get(j) instanceof Implication) {
                        Implication im = (Implication) list.get(j);
                        if (im.r.e(s))
                            for (Statement aList : list)
                                if (im.l.e(aList)) {
                                    s = im.r;
                                    success = true;
                                    break m;
                                }
                    }
            }
            if (!success) {
                System.out.println("Доказательство некорректно начиная с высказывания номер " + (list.size() + 1));
                break;
            }

            list.add(s);
            line = in.readLine();
        }

        if (success)
            System.out.println("Доказательство корректно");

        System.out.println(System.currentTimeMillis() - time);
    }

    public static boolean isEquivalentToAxiom(Statement s) {
        for (Statement axiom : axioms) {
            if (s.hasFormOfThisStatement(axiom))
                return true;
        }
        return s.isEleventhAxiom() || s.isTwelfthAxiom();
    }

    private static void intiAxioms() throws ParseException {
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
