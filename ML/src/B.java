import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class B {
    static ArrayList<Statement> axioms = new ArrayList<Statement>();
    static ArrayList<Statement> hypothesis = new ArrayList<Statement>();

    public static void main(String[] args) throws ParseException, IOException {
        BufferedReader in = new BufferedReader(new FileReader(args[0]));

        ArrayList<Statement> list = new ArrayList<Statement>();

        intiAxioms();

        long time = System.currentTimeMillis();

        String line = in.readLine();
        String[] parts = line.split("\\|\\-");
        String provableStatement = parts[1];
        parts = parts[0].split(",");

        for (int i = 0; i < parts.length - 1; i++)
            hypothesis.add(Parser.parse(parts[i]));
        Statement assumption = Parser.parse(parts[parts.length - 1]);

        line = in.readLine();
        while (line != null) {
            list.add(Parser.parse(line));
            line = in.readLine();
        }
        in.close();

        System.out.println(System.currentTimeMillis() - time);

        PrintWriter out = new PrintWriter("b.out");
        TemplateOutput tOInsertA = new TemplateOutput("toinsert_dt1a.txt");
        TemplateOutput tOInsertAB = new TemplateOutput("toinsert_dt2ab.txt");
        TemplateOutput tOInsertABC = new TemplateOutput("toinsert_dt3abc.txt");

        if (hypothesis.size() > 0)
            out.print(hypothesis.get(0).value);
        for (int i = 1; i < hypothesis.size(); i++)
            out.print("," + hypothesis.get(i).value);
        out.println("|-" + provableStatement);

        boolean success = true;
        for (int i = 0; i < list.size(); i++) {
            Statement s = list.get(i);
            if (s.equ(assumption)) {
                Statement[] ss = {s};
                tOInsertA.print(ss, out);
            } else if (isEquivalentToAxiom(s) || isHypothesis(s)) {
                Statement[] ss = {s, assumption};
                tOInsertAB.print(ss, out);
            } else {
                success = false;
                m:
                for (int j = 0; j < i; j++)
                    if (list.get(j).type == Statement.Type.IMPLICATION && list.get(j).r.equ(s))
                        for (int l = 0; l < i; l++)
                            if (list.get(j).l.equ(list.get(l))) {
                                Statement[] ss = {assumption, list.get(l), s};
                                tOInsertABC.print(ss, out);
                                success = true;
                                break m;
                            }
                if (!success) {
                    System.out.println("Доказательство некорректно начиная с высказывания номер " + (i + 1));
                    break;
                }
            }
        }

        System.out.println(System.currentTimeMillis() - time);
        out.close();
    }

    public static boolean isEquivalentToAxiom(Statement s) {
        for (Statement axiom : axioms) {
            if (s.hasFormOfThisStatement(axiom))
                return true;
        }
        return false;
    }

    public static boolean isHypothesis(Statement s) {
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
}
