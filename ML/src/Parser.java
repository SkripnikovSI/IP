class Parser {
    private static String expression;
    private static int index;

    public static Statement parse(String expression) throws ParseException {
        Parser.expression = expression.replaceAll("\\s+", "") + "@";
        index = 0;
        return formula(true);
    }

    private static Statement formula(boolean isFirst)
            throws ParseException {
        Statement s = factor();
        char nextChar = getChar();
        while (nextChar != '@' && nextChar != ')') {
            if (nextChar == '&') {
                s = conjunction(s);
            } else if (nextChar == '|') {
                s = disjunction(s);
            } else if (nextChar == '-') {
                getChar();
                //check
                s = implication(s);
            }
            else {
                throw new ParseException(expression, index);
            }
            nextChar = getChar();
        }
        if (isFirst && nextChar == ')') {
            throw new ParseException(expression, index);
        }
        returnChar();
        return s;
    }

    private static char getChar() {
        return expression.charAt(index++);
    }

    private static void returnChar() {
        index--;
    }

    private static Statement getVariable() {
        int start = index, end = index;
        char c = getChar();
        while (Character.isLetter(c) || Character.isDigit(c)) {
            c = getChar();
            end++;
        }
        returnChar();
        String value = expression.substring(start, end);
        return new Statement(null, null, Statement.Type.VARIABLE, value);
    }

    private static Statement factor() throws ParseException {
        char nextChar = getChar();
        if (Character.isLetter(nextChar) || Character.isDigit(nextChar)) {
            returnChar();
            return getVariable();
        } else if (nextChar == '(') {
            Statement result = formula(false);
            if (getChar() == ')') {
                return result;
            }
            throw new ParseException(expression, index);
        } else if (nextChar == '!') {
            return new Statement(null, factor(), Statement.Type.INVERSION, null);
        }
        throw new ParseException(expression, index);
    }

    private static Statement conjunction(Statement left)
            throws ParseException {
        Statement result = new Statement(left, factor(), Statement.Type.CONJUNCTION, null);
        if (getChar() == '&')
            result = conjunction(result);
        else
            returnChar();
        return result;
    }

    private static Statement disjunction(Statement left) throws ParseException {
        Statement right = factor();
        if(getChar() == '&')
            right = conjunction(right);
        else
            returnChar();
        Statement result = new Statement(left, right, Statement.Type.DISJUNCTION, null);
        if (getChar() == '|')
            result = disjunction(result);
        else
            returnChar();
        return result;
    }

    private static Statement implication(Statement left) throws ParseException {
        return new Statement(left,  formula(false), Statement.Type.IMPLICATION, null);
    }
}
