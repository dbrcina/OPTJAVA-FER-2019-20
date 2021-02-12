package hr.fer.zemris.optjava.dz8.function;

/**
 * An implementation of {@link IFunction} interface.
 */
public class TangensHyperbolicusTransferFunction implements IFunction {

    @Override
    public double valueAt(double x) {
        return 2 * sigma(x) - 1;
    }

    /**
     * @param x point x
     * @return sigma value for provided <i>x</i>.
     */
    private double sigma(double x) {
        return 1.0 / (1 + Math.exp(-x));
    }

}
