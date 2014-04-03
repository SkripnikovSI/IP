import java.util.ArrayList;

class Parser {
    private static String expression;
    private static int index;

    public static Statement parse(String expression) throws ParseException {
        Parser.expression = expression.replaceAll("\\s+", "") + ";";
        index = 0;
        return implication();
    }

    private static char getChar() {
        return expression.charAt(index++);
    }

    private static void returnChar() {
        index--;
    }

    private static Statement implication() throws ParseException {
        Statement s = disjunction();
        char nextChar = getChar();
        if (nextChar == '-') {
            if (getChar() != '>')
                throw new ParseException(expression, index);
            s = new Implication(s, implication());
        } else
            returnChar();
        return s;
    }

    private static Statement disjunction() throws ParseException {
        Statement l = conjunction();
        char nextChar = getChar();
        while (nextChar == '|') {
            l = new Disjunction(l, conjunction());
            nextChar = getChar();
        }
        returnChar();
        return l;
    }

    private static Statement conjunction() throws ParseException {
        Statement l = unary();
        char nextChar = getChar();
        while (nextChar == '&') {
            l = new Conjunction(l, unary());
            nextChar = getChar();
        }
        returnChar();
        return l;
    }

    private static Statement unary() throws ParseException {
        char nextChar = getChar();
        if (Character.isLetter(nextChar) && Character.isUpperCase(nextChar)) {
            return predicate();
        } else if (nextChar == '!') {
            return new Inversion(unary());
        } else if (nextChar == '(') {
            Statement result = implication();
            if (getChar() != ')')
                throw new ParseException(expression, index);
            return result;
        } else if (nextChar == '@') {
            return new UniversalQuantifier(variable(), unary());
        } else if (nextChar == '?') {
            return new ExistentialQuantifier(variable(), unary());
        } else {
            throw new ParseException(expression, index);
        }
    }

    private static Statement predicate() throws ParseException {
        int start = index - 1, end = index;
        char nextChar;
        while (Character.isDigit(nextChar = getChar()))
            end++;
        String name = expression.substring(start, end);
        ArrayList<Term> list = null;
        if (nextChar == '(') {
            list = terms();
            if (getChar() != ')')
                throw new ParseException(expression, index);
        } else
            returnChar();
        return new Predicate(name, list);
    }

    private static Term term() throws ParseException {
        char nextChar = getChar();
        if (nextChar == '(') {
            Term t = term();
            if (getChar() != ')')
                throw new ParseException(expression, index);
            return t;
        } else {
            returnChar();
            Term t = variable();
            nextChar = getChar();
            if (nextChar == '(') {
                ArrayList<Term> list = terms();
                if (getChar() != ')')
                    throw new ParseException(expression, index);
                return new Function(t.name, list);
            } else {
                returnChar();
                return t;
            }
        }
    }

    private static Variable variable() throws ParseException {
        int start = index, end = index + 1;
        char nextChar = getChar();
        if (!Character.isLetter(nextChar) || !Character.isLowerCase(nextChar))
            throw new ParseException(expression, index);
        while (Character.isDigit(getChar()))
            end++;
        returnChar();
        String value = expression.substring(start, end);
        return new Variable(value);
    }

    private static ArrayList<Term> terms() throws ParseException {
        ArrayList<Term> list = new ArrayList<Term>();
        list.add(term());
        while (getChar() == ',')
            list.add(term());
        returnChar();
        return list;
    }
}
