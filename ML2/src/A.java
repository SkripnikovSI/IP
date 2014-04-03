import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;

public class A {
    static ArrayList<Statement> axioms = new ArrayList<Statement>();
    static ArrayList<Statement> hypothesis = new ArrayList<Statement>();

    public static void main(String[] args) throws ParseException, IOException {
        BufferedReader in = new BufferedReader(new FileReader(args[0]));
        PrintWriter out = new PrintWriter("a.out");
        ArrayList<Statement> list = new ArrayList<Statement>();
        intiAxioms();
        TemplateOutput tOInsertA = new TemplateOutput("toinsert_dt1a.txt");
        TemplateOutput tOInsertAB = new TemplateOutput("toinsert_dt2ab.txt");
        TemplateOutput tOInsertABC = new TemplateOutput("toinsert_dt3abc.txt");
        TemplateOutput tOInsertUQ = new TemplateOutput("toinsert_dt4uq.txt");
        TemplateOutput tOInsertEQ = new TemplateOutput("toinsert_dt5eq.txt");

        String line = in.readLine();

        String[] parts = line.split("\\|\\-");
        String provableStatement = parts[1];
        parts = parts[0].split(",");

        for (int i = 0; i < parts.length - 1; i++)
            hypothesis.add(Parser.parse(parts[i]));
        Statement assumption = Parser.parse(parts[parts.length - 1]);

        if (hypothesis.size() > 0)
            out.print(hypothesis.get(0).value);
        for (int i = 1; i < hypothesis.size(); i++)
            out.print("," + hypothesis.get(i).value);
        out.println("|-" + provableStatement);

        line = in.readLine();

        boolean success = true;

        HashSet<String> assumptionFreeVariables = assumption.getFreeVariables();

        while (line != null) {
            Statement s = Parser.parse(line);
            if (s.e(assumption)) {
                Statement[] ss = {s};
                tOInsertA.print(ss, out);
                list.add(s);
                line = in.readLine();
                continue;
            }
            if (isEquivalentToAxiom(s) || isHypothesis(s)) {
                Statement[] ss = {s, assumption};
                tOInsertAB.print(ss, out);
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
                                if (im2.l.e(im.l) && im2.r.e(uq.s) && !assumptionFreeVariables.contains(uq.v.name) &&
                                        !im.l.getFreeVariables().contains(uq.v.name)) {
                                    im.l = im2.l;
                                    uq.s = im2.r;
                                    Statement[] ss = {assumption, im.l, uq.s, uq};
                                    tOInsertUQ.print(ss, out);
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
                                if (im2.r.e(im.r) && im2.l.e(eq.s) &&  !assumptionFreeVariables.contains(eq.v.name) &&
                                        !im.r.getFreeVariables().contains(eq.v.name)) {
                                    im.r = im2.r;
                                    eq.s = im2.l;
                                    Statement[] ss = {assumption, eq.s, im.r, eq};
                                    tOInsertEQ.print(ss, out);
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
                                    Statement[] ss = {assumption, aList, s};
                                    tOInsertABC.print(ss, out);
                                    success = true;
                                    break m;
                                }
                    }
            }
            if (!success) {
                System.out.println("Доказательство некорректно или \nневозможно пременить теорему о дедукции \n начиная с высказывания номер " + (list.size() + 1));
                break;
            }

            list.add(s);
            line = in.readLine();
        }

        out.close();
    }

    public static boolean isEquivalentToAxiom(Statement s) {
        for (Statement axiom : axioms) {
            if (s.hasFormOfThisStatement(axiom))
                return true;
        }
        return s.isEleventhAxiom() || s.isTwelfthAxiom();
    }

    public static boolean isHypothesis(Statement s) {
        for (Statement h : hypothesis) {
            if (s.e(h))
                return true;
        }
        return false;
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
