import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class TemplateOutput {
    ArrayList<Statement> list = new ArrayList<Statement>();

    TemplateOutput(String fileName) throws IOException, ParseException {
        BufferedReader in = new BufferedReader(new FileReader(fileName));
        String line = in.readLine();
        while (line != null) {
            list.add(Parser.parse(line));
            line = in.readLine();
        }
    }

    public void print(Statement[] statements, PrintWriter out) {
        HashMap<String, Statement> map = new HashMap<String, Statement>();
        for (Statement aList : list) out.println(aList.substituteStatements(statements, map).value);
    }

    public void inOutput(Statement[] statements, ArrayList<Statement> output) {
        HashMap<String, Statement> map = new HashMap<String, Statement>();
        for (Statement aList : list) output.add(aList.substituteStatements(statements, map));
    }
}
