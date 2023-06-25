package Regression;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

public class LinearModel {
    public double weights, bias;
    protected double speed;
    public static int modelCount = 0;

    public LinearModel() {
        weights = Math.random();
        bias = Math.random();
        this.speed = 0.005;
    }

    public double forward(double x) {
        return bias + (x * weights);
    }

    public double calcLoss(HashMap<Double, Double> testing) {
        AtomicReference<Double> totalLoss = new AtomicReference<>((double) 0);
        testing.forEach((x, y) -> {
            totalLoss.updateAndGet(v -> (v + Math.abs(forward(x) - y)));
        });
        return totalLoss.get();
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
                weights -= speed * 2;
                newLoss = calcLoss(points);
                if (newLoss > loss) {
                    weights += speed;
                }
            }

            bias += speed;
            newLoss = calcLoss(points);
            if (newLoss > loss) {
                bias -= speed * 2;
                newLoss = calcLoss(points);
                if (newLoss > loss) {
                    bias += speed;
                }
            }
        }
    }

    public String toEquation()
    {
        return "r" + (modelCount++) + "(x) = " + weights + " * x +" + bias;
    }
}
