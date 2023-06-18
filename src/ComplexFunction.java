import org.jetbrains.annotations.NotNull;
import java.awt.*;
import java.util.ArrayList;

public class ComplexFunction
{
    public ArrayList<BasicFunction> terms;
    public ComplexFunction()
    {
        terms = new ArrayList<>();
    }

    /* Get the sum of all the terms */
    double evaluate(double x)
    {
        double result = 0;
        for(BasicFunction f : terms)
        {
            double val = f.evaluate(x);
            result += val;
        }
        return result;
    }

    public ComplexFunction inverse()
    {
        ComplexFunction inv = new ComplexFunction();
        for(BasicFunction f : terms)
        {
            inv.terms.add(f.inverse());
        }
        return inv;
    }

    public ComplexFunction derivative()
    {
        ComplexFunction dif = new ComplexFunction();
        for(BasicFunction f : terms)
        {
            dif.terms.add(f.derivative());
        }
        return dif;
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
        return func.toString();
    }

    public static @NotNull ComplexFunction parseFunction(String data)
    {
        ComplexFunction func = new ComplexFunction();
        String[] terms = data.split(" \\+ ");
        for(String term : terms)
        {
            func.terms.add(BasicFunction.parseFunction(term));
        }
        return func;
    }
}
