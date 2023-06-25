package Regression;

public class PolynomialModel extends LinearModel
{
    public double degree;
    public PolynomialModel(double degree) {
        super();
        this.degree = degree;
    }

    @Override
    public double forward(double x)
    {
        return Math.pow(x, degree) * weights + bias;
    }

    @Override
    public String toEquation()
    {
        return "r" + (modelCount++) + "(x) = " + weights + " * x^" + degree + " + " + bias;
    }
}
