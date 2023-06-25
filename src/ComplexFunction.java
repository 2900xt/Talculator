import org.jetbrains.annotations.NotNull;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ComplexFunction
{
    public ArrayList<BasicFunction> terms;
    public ComplexFunction next;
    public boolean isDerivative;
    public String name;
    public Color color;
    public ComplexFunction()
    {
        next = null;
        terms = new ArrayList<>();
        isDerivative = false;
        color = new Color(
            (int) (Math.random() * Integer.MAX_VALUE)
        );
    }

    public void addFactor(ComplexFunction factor)
    {
        // Insert into the linked list
        assert factor != null;
        factor.next = next;
        this.next = factor;
    }

    /* Get the sum of all the terms */
    double evaluate(double x)
    {
        if(isDerivative)
        {
            return evaluateDerivative(x);
        }
        else
        {
            return actuallyEvaluate(x);
        }
    }

    double actuallyEvaluate(double x)
    {
        double result = 0;
        for(BasicFunction f : terms)
        {
            double val = f.evaluate(x);
            result += val;
        }

        if(next != null)
        {
            result *= next.evaluate(x);
        }
        return result;
    }

    public static final double limitVal = 0.0001;

    double evaluateDerivative(double x)
    {
        return (actuallyEvaluate(x - limitVal) - actuallyEvaluate(x + limitVal)) / ((x - limitVal) - (x + limitVal));
    }

    public ComplexFunction inverse()
    {
        ComplexFunction inv = new ComplexFunction();
        for(BasicFunction f : terms)
        {
            inv.terms.add(f.inverse());
        }

        if(next != null)
        {
            inv.next = next.inverse();
        }
        return inv;
    }

    public String toString()
    {
        StringBuilder func = new StringBuilder();
        for(int i = 0; i < terms.size() - 1; i++)
        {
            func.append(terms.get(i)).append(" + ");
        }
        if(terms.isEmpty()) return "";
        func.append(terms.get(terms.size() - 1));

        String out = func.toString();
        if(next != null)
        {
            out = "(" + out + ")(" + next.toString().replace("null(x) = ", "") + ")";
        }

        if(isDerivative)
        {
            out = "d/dx(" + out + ")";
        }

        return name + "(x) = " + out;
    }

    public static @NotNull ComplexFunction parseFunction(String data, boolean firstExp)
    {
        data = data.replace(" ", "");

        ComplexFunction func = new ComplexFunction();
        func.next = null;

        if(firstExp)
        {
            func.name = data.substring(0, data.indexOf('('));
            data = data.substring(data.indexOf('=') + 1);
        }

        String expression = String.copyValueOf(data.toCharArray());
        if(data.contains("d/dx"))
        {
            expression = expression.substring(4);
            func.isDerivative = true;
        }
        else if(data.contains("inv")) expression = expression.substring(3);


        String nextExpression = null;

        if(expression.charAt(0) == '(')
        {
            int nextExprInd = expression.indexOf(")(");
            nextExpression = expression.substring(nextExprInd + 2, expression.length() - 1);
            expression = expression.substring(1, nextExprInd);
        }


        String[] terms = expression.split("\\+");
        for(String term : terms)
        {
            System.out.println(term);
            func.terms.add(BasicFunction.parseFunction(term));
        }

        if(nextExpression != null)
        {
            func.next = ComplexFunction.parseFunction(nextExpression, false);
        }
        if(data.contains("inv")) return func.inverse();
        return func;
    }

    public static int regressionCount = 0;

    public static ComplexFunction fromRegression(HashMap<Double, Double> data)
    {
        final double[] lowestX = { Double.POSITIVE_INFINITY };
        final double[] lowestY = { Double.POSITIVE_INFINITY };
        final double[] highestX = { Double.NEGATIVE_INFINITY };
        final double[] highestY = { Double.NEGATIVE_INFINITY };
        data.forEach((x, y) ->
                {
                    lowestX[0] = Math.min(x, lowestX[0]);
                    lowestY[0] = Math.min(y, lowestY[0]);
                    highestX[0] = Math.max(x, highestX[0]);
                    highestY[0] = Math.max(y, highestY[0]);
                }
        );
        // y - mx = b
        double weight = (highestY[0] - lowestY[0]) / (highestX[0] - lowestX[0]);
        double bias = lowestY[0] - weight * lowestX[0];

        return ComplexFunction.parseFunction("r" + (++regressionCount) + "(x) = " + weight + " * x + " + bias, true);
    }
}