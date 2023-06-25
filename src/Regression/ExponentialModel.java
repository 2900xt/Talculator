package Regression;

public class ExponentialModel extends LinearModel
{
    // Weight is exponent, bias is coefficient
    public ExponentialModel()
    {
        super();
    }

    public double forward(double x)
    {
        return Math.pow(weights, x) * bias;
    }

    public String toEquation()
    {
        return "r" + (modelCount++) + "(x) = " + weights + "^(" + bias + " * x)";
    }
}
