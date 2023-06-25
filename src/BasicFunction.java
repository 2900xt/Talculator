import static java.lang.Double.NaN;

public class BasicFunction
{
    public double a, b;
    public FunctionType type;
    public TrigFunctionType trigType;
    public enum TrigFunctionType
    {
        SINE,
        COSINE,
        TANGENT,
        COSECANT,
        SECANT,
        COTANGENT
    };

    public enum FunctionType
    {
        /* Polynomial Functions -
         *
         * a = coefficient of x
         * b = power of x
         * f(x) = a * x^b
         */
        Polynomial,

        /* Exponential Functions -
         *
         * a = coefficient of x
         * b = base
         * f(x) = b^(a * x)
         */
        Exponential,

        /* Logarithmic Functions -
         *
         * a = coefficient of x
         * b = base
         * f(x)= log b (a * x)
         */
        Logarithmic,

        /* Trigonometric Functions -
         *
         * a = power of function
         * b = coefficient of term
         * f(x) = b * sin^a (  x )
         */
        Trigonometric,

        /* Same thing as regular trig functions */
        InverseTrig,
    }

    public BasicFunction(double a, double b, FunctionType type)
    {
        this.a = a;
        this.b = b;
        this.type = type;
    }

    public double evaluateTrigFunction(double x)
    {
        double result = NaN;
        switch(trigType)
        {
            case SINE ->
            {
                result = b * Math.pow(Math.sin(x), a);
                break;
            }
            case COSINE ->
            {
                result = b * Math.pow(Math.cos(x), a);
                break;
            }
            case TANGENT ->
            {
                result = b * Math.pow(Math.sin(x) / Math.cos(x), a);
                break;
            }
            case COSECANT ->
            {
                result = b * Math.pow(1 / Math.sin(x), a);
                break;
            }
            case SECANT ->
            {
                result = b * Math.pow(1 / Math.cos(x), a);
            }
            case COTANGENT ->
            {
                result = b * Math.pow(Math.cos(x) / Math.sin(x), a);
            }
        }
        return result;
    }

    public double evaluateInverseTrigFunction(double x)
    {
        double result = NaN;
        switch(trigType)
        {
            case SINE ->
            {
                result = b * Math.pow(Math.asin(x), a);
                break;
            }
            case COSINE ->
            {
                result = b * Math.pow(Math.acos(x), a);
                break;
            }
            case TANGENT ->
            {
                result = b * Math.pow(Math.atan(x), a);
                break;
            }
            case COSECANT ->
            {
                result = b * Math.pow(Math.asin(1 / x), a);
                break;
            }
            case SECANT ->
            {
                result = b * Math.pow(Math.acos(1 / x), a);
            }
            case COTANGENT ->
            {
                result = b * Math.pow((Math.PI / 2) - Math.atan(x), a);
            }
        }
        return result;
    }

    public double evaluate(double x)
    {
        double result = NaN;
        switch (type)
        {
            case Exponential -> {
                result = Math.pow(b, a * x);
            }
            case Polynomial -> {
                result = a * Math.pow(x, b);
            }
            case Trigonometric -> {
                result = evaluateTrigFunction(x);
            }
            case Logarithmic -> {
                result = Math.log(a * x) / Math.log(b);
            }
            case InverseTrig -> {
                result = evaluateInverseTrigFunction(x);
            }
            default -> {
                System.err.println("Unknown Function type for basic functions");
            }
        }
        return result;
    }

    public BasicFunction inverse()
    {
        if(type == FunctionType.Trigonometric)
        {
            BasicFunction f = new BasicFunction(a, b, FunctionType.InverseTrig);
            f.trigType = trigType;
            return f;
        }

        if(type == FunctionType.Polynomial)
        {
            return new BasicFunction(a, 1/b, type);
        }

        if(type == FunctionType.Exponential)
        {
            return new BasicFunction(a, b, FunctionType.Logarithmic);
        }

        if(type == FunctionType.Logarithmic)
        {
            return new BasicFunction(a, b, FunctionType.Exponential);
        }

        return null;
    }

    public BasicFunction derivative()
    {
        /* Power rule */
        if(type == FunctionType.Polynomial)
        {
            return new BasicFunction(b * a, b - 1, FunctionType.Polynomial);
        }
        if(type == FunctionType.Trigonometric)
        {
            BasicFunction f = null;
            switch (trigType)
            {
                case SINE ->
                {
                    f = new BasicFunction(a, b, FunctionType.Trigonometric);
                    f.trigType = TrigFunctionType.COSINE;
                }
                case COSINE ->
                {
                    f = new BasicFunction(a, -b, FunctionType.Trigonometric);
                    f.trigType = TrigFunctionType.SINE;
                }
                case TANGENT ->
                {
                    f = new BasicFunction(a * 2, b, FunctionType.Trigonometric);
                    f.trigType = TrigFunctionType.SECANT;
                }
            }
            return f;
        }

        return null;

    }

    public String toString()
    {
        String aString = "" + round(a, 2);
        if(aString.equals("1.0"))
        {
            aString = "";
        }

        String bString = "" + round(b, 2);
        if(bString.equals("1.0"))
        {
            bString = "";
        }


        switch (type) {
            case Polynomial -> {
                if (bString.equals("0.0")) {
                    return aString;
                }
                if (aString.equals("") && bString.equals("")) {
                    return "x";
                }
                if (aString.equals("")) {
                    return "x^" + bString;
                }
                if (bString.equals("")) {
                    return aString + " * x";
                }
                return aString + " * x^" + bString;
            }
            case Exponential -> {
                if (bString.equals("2.72") && aString.equals("")) {
                    return "e^(x)";
                }
                if (aString.equals("")) {
                    return bString + "^(x)";
                }
                if (bString.equals("2.72")) {
                    return "e^(" + aString + " * x)";
                }
                return bString + "^(" + aString + " * x)";
            }
            case Logarithmic -> {
                if (bString.equals("2.72")) {
                    if (aString.equals("")) {
                        return "ln(x)";
                    }
                    return "ln(" + aString + " * x)";
                }

                if (bString.equals("10.0")) {
                    if (aString.equals("")) {
                        return "log(x)";
                    }
                    return "log(" + aString + " * x)";
                }

                if (aString.equals("")) {
                    return "log" + bString + "(x)";
                }
                return "log" + bString + "(" + aString + " * x)";
            }
            case InverseTrig, Trigonometric -> {
                String function = "";
                switch (trigType) {
                    case SINE -> function = "sin";
                    case COSINE -> function = "cos";
                    case TANGENT -> function = "tan";
                    case COSECANT -> function = "csc";
                    case SECANT -> function = "sec";
                    case COTANGENT -> function = "cot";
                }
                if (type == FunctionType.InverseTrig) {
                    function = "arc" + function;
                }
                if (bString.equals("") && aString.equals("")) {
                    return function + "(x)";
                }
                if (bString.equals("")) {
                    return function + aString + "(x)";
                }
                if (aString.equals("")) {
                    return bString + "*" + function + "(x)";
                }
                return bString + "*" + function + aString + "(x)";
            }
            default -> {
                return null;
            }
        }
    }

    private static double round(double val, double place)
    {
        return Math.round(val * Math.pow(10, place)) / Math.pow(10, place);
    }

    public static BasicFunction parseFunction(String term)
    {
        /* Trigonometric */
        if(term.contains("sin"))
        {
            double coefficient = 1;
            int asteriskInd = term.indexOf("*");
            if(asteriskInd != -1)
            {
                coefficient = Double.parseDouble(term.substring(0, asteriskInd));
            }

            double power = 1;
            int parInd = term.indexOf("(");
            int sinInd = term.indexOf("n") + 1;
            if(sinInd != parInd)
            {
                power = Double.parseDouble(term.substring(sinInd, parInd));
            }
            BasicFunction f = new BasicFunction(power, coefficient, FunctionType.Trigonometric);

            if(term.contains("arc"))
            {
                f.type = FunctionType.InverseTrig;
            }

            f.trigType = TrigFunctionType.SINE;

            return f;
        }
        if(term.contains("cos"))
        {
        double coefficient = 1;
        int asteriskInd = term.indexOf("*");
        if(asteriskInd != -1)
        {
            coefficient = Double.parseDouble(term.substring(0, asteriskInd));
        }

        double power = 1;
        int parInd = term.indexOf("(");
        int sinInd = term.indexOf("s") + 1;
        if(sinInd != parInd)
        {
            power = Double.parseDouble(term.substring(sinInd, parInd));
        }
        BasicFunction f = new BasicFunction(power, coefficient, FunctionType.Trigonometric);
        f.trigType = TrigFunctionType.COSINE;


        if(term.contains("arc"))
        {
            f.type = FunctionType.InverseTrig;
        }

        return f;
    }
        if(term.contains("tan"))
        {
            double coefficient = 1;
            int asteriskInd = term.indexOf("*");
            if(asteriskInd != -1)
            {
                coefficient = Double.parseDouble(term.substring(0, asteriskInd));
            }

            double power = 1;
            int parInd = term.indexOf("(");
            int sinInd = term.indexOf("n") + 1;
            if(sinInd != parInd)
            {
                power = Double.parseDouble(term.substring(sinInd, parInd));
            }
            BasicFunction f = new BasicFunction(power, coefficient, FunctionType.Trigonometric);
            f.trigType = TrigFunctionType.TANGENT;
            if(term.contains("arc"))
            {
                f.type = FunctionType.InverseTrig;
            }
            return f;
        }
        if(term.contains("csc"))
        {
            double coefficient = 1;
            int asteriskInd = term.indexOf("*");
            if(asteriskInd != -1)
            {
                coefficient = Double.parseDouble(term.substring(0, asteriskInd));
            }

            double power = 1;
            int parInd = term.indexOf("(");
            int sinInd = term.indexOf("csc") + 3;
            if(sinInd != parInd)
            {
                power = Double.parseDouble(term.substring(sinInd, parInd));
            }
            BasicFunction f = new BasicFunction(power, coefficient, FunctionType.Trigonometric);
            f.trigType = TrigFunctionType.COSECANT;
            if(term.contains("arc"))
            {
                f.type = FunctionType.InverseTrig;
            }
            return f;
        }
        if(term.contains("sec"))
        {
            double coefficient = 1;
            int asteriskInd = term.indexOf("*");
            if(asteriskInd != -1)
            {
                coefficient = Double.parseDouble(term.substring(0, asteriskInd));
            }

            double power = 1;
            int parInd = term.indexOf("(");
            int sinInd = term.indexOf("sec") + 3;
            if(sinInd != parInd)
            {
                power = Double.parseDouble(term.substring(sinInd, parInd));
            }
            BasicFunction f = new BasicFunction(power, coefficient, FunctionType.Trigonometric);
            f.trigType = TrigFunctionType.SECANT;
            if(term.contains("arc"))
            {
                f.type = FunctionType.InverseTrig;
            }
            return f;
        }
        if(term.contains("cot"))
        {
            double coefficient = 1;
            int asteriskInd = term.indexOf("*");
            if(asteriskInd != -1)
            {
                coefficient = Double.parseDouble(term.substring(0, asteriskInd));
            }

            double power = 1;
            int parInd = term.indexOf("(");
            int sinInd = term.indexOf("t") + 1;
            if(sinInd != parInd)
            {
                power = Double.parseDouble(term.substring(sinInd, parInd));
            }
            BasicFunction f = new BasicFunction(power, coefficient, FunctionType.Trigonometric);
            f.trigType = TrigFunctionType.COTANGENT;
            if(term.contains("arc"))
            {
                f.type = FunctionType.InverseTrig;
            }
            return f;
        }

        /* Logarithmic */
        if(term.contains("log") || term.contains("ln"))
        {
            double base = Math.E;
            int parIndex = term.indexOf("(");
            if(term.contains("log"))
            {
                if(parIndex != 3)
                {
                    base = Double.parseDouble(term.substring(3, parIndex));
                } else
                {
                    base = 10;
                }
            }

            double coefficient = 1;
            int asteriskInd = term.indexOf("*");
            if(asteriskInd != -1)
            {
                coefficient = Double.parseDouble(term.substring(parIndex + 1, asteriskInd));
            }

            return new BasicFunction(coefficient, base, FunctionType.Logarithmic);
        }

        /* Exponential */
        if(term.contains("^("))
        {
            int baseInd = term.indexOf("^");
            double base = Math.E;
            if(term.charAt(0) != 'e' && term.charAt(1) != 'e')
            {
                base = Double.parseDouble(term.substring(0, baseInd));
            }
            int asteriskInd = term.indexOf("*");
            double coefficient = 1;
            if(asteriskInd != -1)
            {
                int parInd = term.indexOf("(");
                coefficient = Double.parseDouble(term.substring(parInd + 1, asteriskInd));
            }

            return new BasicFunction(coefficient, base, FunctionType.Exponential);
        }

        /* Multiple degree polynomial */
        if(term.contains("x^"))
        {
            int asteriskInd = term.indexOf("*");
            double coefficient = 1;
            if(asteriskInd != -1)
            {
                coefficient = Double.parseDouble(term.substring(0, asteriskInd));
            }

            int degInd = term.indexOf("^") + 1;
            double degree = Double.parseDouble(term.substring(degInd));
            return new BasicFunction(coefficient, degree, FunctionType.Polynomial);
        }

        /* Linear equation */
        if(term.contains("x"))
        {
            int asteriskInd = term.indexOf("*");
            double coefficient = 1;
            if(asteriskInd != -1)
            {
                coefficient = Double.parseDouble(term.substring(0, asteriskInd));
            }
            return new BasicFunction(coefficient, 1, FunctionType.Polynomial);
        }

        /* Constant */
        double coefficient = Double.parseDouble(term);
        return new BasicFunction(coefficient, 0, FunctionType.Polynomial);
    }
}
