package hr.fer.zemris.optjava.dz4.part1.function;

import org.apache.commons.math3.linear.RealMatrix;

/**
 * An implementation of {@link IFunction} interface.
 */
public class FunctionImpl implements IFunction {

    private RealMatrix coefficients;
    private RealMatrix results;

    /**
     * Constructor.
     *
     * @param coefficients coefficients.
     * @param results      results.
     */
    public FunctionImpl(RealMatrix coefficients, RealMatrix results) {
        this.coefficients = coefficients;
        this.results = results;
    }

    @Override
    public double valueAt(double[] point) {
        double a = point[0];
        double b = point[1];
        double c = point[2];
        double d = point[3];
        double e = point[4];
        double f = point[5];
        double error = 0;
        for (int i = 0; i < coefficients.getRowDimension(); i++) {
            double x1 = coefficients.getEntry(i, 0);
            double x2 = coefficients.getEntry(i, 1);
            double x3 = coefficients.getEntry(i, 2);
            double x4 = coefficients.getEntry(i, 3);
            double x5 = coefficients.getEntry(i, 4);
            double y = results.getEntry(i, 0);
            double function = a * x1
                    + b * x1 * x1 * x1 * x2
                    + c * Math.exp(d * x3) * (1 + Math.cos(e * x4))
                    + f * x4 * x5 * x5
                    - y;
            error += function * function;
        }
        return -error;
    }
}
