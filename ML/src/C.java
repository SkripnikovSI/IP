import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class C {
    private static ArrayList<Statement> axioms = new ArrayList<Statement>();
    private static TemplateOutput tOInsertA;
    private static TemplateOutput tOInsertAB;
    private static TemplateOutput tOInsertABC;
    private static TemplateOutput tOAssumptionExclusion;
    private static TemplateOutput tOCFF;
    private static TemplateOutput tOCFT;
    private static TemplateOutput tOCTF;
    private static TemplateOutput tOCTT;
    private static TemplateOutput tODFF;
    private static TemplateOutput tODFT;
    private static TemplateOutput tODTF;
    private static TemplateOutput tODTT;
    private static TemplateOutput tOIMFF;
    private static TemplateOutput tOIMFT;
    private static TemplateOutput tOIMTF;
    private static TemplateOutput tOIMTT;
    private static TemplateOutput tOINF;
    private static TemplateOutput tOINT;

    private static boolean[] badEvaluation = {};
    private static boolean allOK = true;

    public static void main(String[] args) throws ParseException, IOException {
        long time = System.currentTimeMillis();
        BufferedReader in = new BufferedReader(new FileReader(args[0]));
        intiAxioms();
        initTemplateOutputs();
        Statement s = Parser.parse(in.readLine());
        String[] v = s.getVariable();
        ArrayList<Statement> output = generatingOutput(v, new boolean[v.length], 0, s);
        System.out.println(System.currentTimeMillis() - time);
        PrintWriter out = new PrintWriter("c.out");
        if (allOK)
            for (Statement anOutput : output) out.println(anOutput.value);
        else {
            out.print("Высказывание ложно при " + v[0] + "=");
            if (badEvaluation[0])
                out.print("И");
            else
                out.print("Л");
            for (int i = 1; i < v.length; i++) {
                out.print(", " + v[i] + "=");
                if (badEvaluation[i])
                    out.print("И");
                else
                    out.print("Л");
            }
        }
        out.close();
    }

    public static ArrayList<Statement> generatingOutput(String[] v, boolean[] evaluation, int deep, Statement s) {
        if (!allOK)
            return null;
        ArrayList<Statement> hypothesis = new ArrayList<Statement>();
        ArrayList<Statement> output = new ArrayList<Statement>();
        for (int i = 0; i < deep; i++) {
            if (evaluation[i])
                hypothesis.add(new Statement(null, null, Statement.Type.VARIABLE, v[i]));
            else
                hypothesis.add(new Statement(null, new Statement(null, null, Statement.Type.VARIABLE, v[i]),
                        Statement.Type.INVERSION, null));
        }
        if (deep == evaluation.length) {
            if (!traversalOfTree(hypothesis, s, output)) {
                allOK = false;
                badEvaluation = evaluation;
            }
        } else if (allOK) {
            boolean[] evaluationCopy = new boolean[evaluation.length];
            System.arraycopy(evaluation, 0, evaluationCopy, 0, deep);
            evaluation[deep] = true;

            ArrayList<Statement> a = generatingOutput(v, evaluation, deep + 1, s);
            if (!allOK)
                return null;
            ArrayList<Statement> b = generatingOutput(v, evaluationCopy, deep + 1, s);
            if (!allOK)
                return null;
            Statement assumption = new Statement(null, null, Statement.Type.VARIABLE, v[deep]);
            deductionTheorem(hypothesis, a, assumption, output);
            deductionTheorem(hypothesis, b, new Statement(null, assumption, Statement.Type.INVERSION, null), output);
            Statement[] ss = {assumption, s};
            tOAssumptionExclusion.inOutput(ss, output);
        }
        return output;
    }

    public static boolean traversalOfTree(ArrayList<Statement> hypothesis, Statement s, ArrayList<Statement> output) {
        if (s.type == Statement.Type.INVERSION) {
            Statement[] ss = {s.r};
            if (traversalOfTree(hypothesis, s.r, output)) {
                tOINT.inOutput(ss, output);
                return false;
            } else {
                tOINF.inOutput(ss, output);
                return true;
            }
        } else if (s.type != Statement.Type.VARIABLE) {
            boolean a = traversalOfTree(hypothesis, s.l, output);
            boolean b = traversalOfTree(hypothesis, s.r, output);
            Statement[] ab = {s.l, s.r};
            Statement[] ba = {s.r, s.l};
            if (a && b) {
                if (s.type == Statement.Type.CONJUNCTION)
                    tOCTT.inOutput(ab, output);
                else if (s.type == Statement.Type.DISJUNCTION)
                    tODTT.inOutput(ab, output);
                else
                    tOIMTT.inOutput(ba, output);
                return true;
            } else if (a) {
                if (s.type == Statement.Type.CONJUNCTION) {
                    tOCTF.inOutput(ba, output);
                    return false;
                } else if (s.type == Statement.Type.DISJUNCTION) {
                    tODTF.inOutput(ab, output);
                    return true;
                } else {
                    tOIMTF.inOutput(ab, output);
                    return false;
                }
            } else if (b) {
                if (s.type == Statement.Type.CONJUNCTION) {
                    tOCFT.inOutput(ab, output);
                    return false;
                } else if (s.type == Statement.Type.DISJUNCTION) {
                    tODFT.inOutput(ba, output);
                    return true;
                } else {
                    tOIMFT.inOutput(ba, output);
                    return true;
                }
            } else {
                if (s.type == Statement.Type.CONJUNCTION) {
                    tOCFF.inOutput(ba, output);
                    return false;
                } else if (s.type == Statement.Type.DISJUNCTION) {
                    tODFF.inOutput(ab, output);
                    return false;
                } else {
                    tOIMFF.inOutput(ba, output);
                    return true;
                }
            }
        } else {
            return isHypothesis(s, hypothesis);
        }
    }

    public static void deductionTheorem(ArrayList<Statement> hypothesis, ArrayList<Statement> list,
                                        Statement assumption, ArrayList<Statement> output) {
        boolean success;
        for (int i = 0; i < list.size(); i++) {
            Statement s = list.get(i);
            if (s.equ(assumption)) {
                Statement[] ss = {s};
                tOInsertA.inOutput(ss, output);
            } else if (isEquivalentToAxiom(s) || isHypothesis(s, hypothesis)) {
                Statement[] ss = {s, assumption};
                tOInsertAB.inOutput(ss, output);
            } else {
                success = false;
                m:
                for (int j = 0; j < i; j++)
                    if (list.get(j).type == Statement.Type.IMPLICATION && list.get(j).r.equ(s))
                        for (int l = 0; l < i; l++)
                            if (list.get(j).l.equ(list.get(l))) {
                                Statement[] ss = {assumption, list.get(l), s};
                                tOInsertABC.inOutput(ss, output);
                                success = true;
                                break m;
                            }
                if (!success) {
                    System.out.println("Доказательство некорректно начиная с высказывания номер " + (i + 1));
                    break;
                }
            }
        }
    }

    public static boolean isEquivalentToAxiom(Statement s) {
        for (Statement axiom : axioms) {
            if (s.hasFormOfThisStatement(axiom))
                return true;
        }
        return false;
    }

    public static boolean isHypothesis(Statement s, ArrayList<Statement> hypothesis) {
        for (Statement h : hypothesis) {
            if (s.equ(h))
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

    private static void initTemplateOutputs() throws IOException, ParseException {
        tOInsertA = new TemplateOutput("toinsert_dt1a.txt");
        tOInsertAB = new TemplateOutput("toinsert_dt2ab.txt");
        tOInsertABC = new TemplateOutput("toinsert_dt3abc.txt");
        tOAssumptionExclusion = new TemplateOutput("to_assumption_exclusion.txt");
        tOCFF = new TemplateOutput("to_cff.txt");
        tOCFT = new TemplateOutput("to_cft.txt");
        tOCTF = new TemplateOutput("to_ctf.txt");
        tOCTT = new TemplateOutput("to_ctt.txt");
        tODFF = new TemplateOutput("to_dff.txt");
        tODFT = new TemplateOutput("to_dft.txt");
        tODTF = new TemplateOutput("to_dtf.txt");
        tODTT = new TemplateOutput("to_dtt.txt");
        tOIMFF = new TemplateOutput("to_imff.txt");
        tOIMFT = new TemplateOutput("to_imft.txt");
        tOIMTF = new TemplateOutput("to_imtf.txt");
        tOIMTT = new TemplateOutput("to_imtt.txt");
        tOINF = new TemplateOutput("to_inf.txt");
        tOINT = new TemplateOutput("to_int.txt");
    }
}
