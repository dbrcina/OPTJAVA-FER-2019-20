package hr.fer.zemris.optjava.dz7.function;

public class SigmoidTransferFunction implements ITransferFunction {

    @Override
    public double valueAt(double x) {
        return 1.0 / (1 + Math.exp(-x));
    }

}
