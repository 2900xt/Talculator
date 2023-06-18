import static java.lang.Double.NaN;

public class BasicFunction
{
    public double a, b;
    public FunctionType type;
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
         * a = power of sine
         * b = coefficient of term
         * f(x) = b * sin a (  x )
         */
        Trigonometric,
    }

    public BasicFunction(double a, double b, FunctionType type)
    {
        this.a = a;
        this.b = b;
        this.type = type;
    }

    public double evaluate(double x)
    {
        switch (type)
        {
            case Exponential -> {
                return Math.pow(b, a * x);
            }
            case Polynomial -> {
                return a * Math.pow(x, b);
            }
            case Trigonometric -> {
                return b * Math.pow(Math.sin(x), a);
            }
            case Logarithmic -> {
                return Math.log(a * x) / Math.log(b);
            }
            default -> {
                System.err.println("Unknown Function type for basic functions");
            }
        }
        return NaN;
    }

    public BasicFunction inverse()
    {
        if(type == FunctionType.Trigonometric)
        {
            System.out.println("Trig inverse function doesn't exist");
            return null;
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
        if(type != FunctionType.Polynomial)
        {
            return null;
        }

        return new BasicFunction(b * a, b - 1, FunctionType.Polynomial);
    }

    public String toString()
    {
        switch (type)
        {
            case Polynomial -> {
                return round(a, 2) + " * x^" + round(b, 2);
            }
            case Exponential -> {
                return round(b, 2) + "^(" + round(a, 2) + " * x)";
            }
            case Logarithmic -> {
                return "log" + round(b, 2) + "(" + round(a, 2) + " * x)";
            }
            case Trigonometric -> {
                return round(b, 2) + "* sin" + round(a, 2) + "(x)";
            }
        }
        return null;
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

            return new BasicFunction(power, coefficient, FunctionType.Trigonometric);
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
            double base = Double.parseDouble(term.substring(0, baseInd));
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
