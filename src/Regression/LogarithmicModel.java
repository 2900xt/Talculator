package Regression;

import java.util.HashMap;

public class LogarithmicModel extends LinearModel
{
    //Weight is base and bias is coefficient
    public LogarithmicModel()
    {
        super();
    }

    public double forward(double x)
    {
        return Math.log(bias * x) / Math.log(weights);
    }

    public void trainTo(HashMap<Double, Double> points, int numEpochs) {
    for (int i = 0; i < numEpochs; i++) {
        double loss = calcLoss(points), newLoss;
        speed = loss / 500;

        if (loss == 0) {
            return;
        }

        //Try to adjust parameters and find a way to get a lower loss
        weights += speed;
        newLoss = calcLoss(points);
        if (newLoss > loss) {
            weights = Math.max(weights - speed * 2, 0.001);
            newLoss = calcLoss(points);
            if (newLoss > loss) {
                weights += speed;
            }
        }

        bias += speed;
        newLoss = calcLoss(points);
        if (newLoss > loss) {
            bias = Math.max(bias - speed * 2, 0.001);
            newLoss = calcLoss(points);
            if (newLoss > loss) {
                bias += speed;
            }
        }
    }
}

    public String toEquation()
    {
        return "r" + (modelCount++) + "(x) = log" + weights + "(" + bias + " * x)";
    }
}
