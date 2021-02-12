package hr.fer.zemris.optjava.dz2.functions;

import org.apache.commons.math3.linear.RealMatrix;

import java.util.Arrays;

/**
 * An implementation of {@link IHFunction} interface. <br> Every method is hardcoded based on
 * linear system from txt file.
 */
public class Function3 implements IHFunction {

    private static final int NUMBER_OF_VARIABLES = 10;

    private RealMatrix coeffiecients;
    private RealMatrix b;

    public Function3(RealMatrix coeffiecients, RealMatrix b) {
        this.coeffiecients = coeffiecients;
        this.b = b;
    }

    @Override
    public RealMatrix hesse(RealMatrix point) {
        return coeffiecients.transpose().scalarMultiply(10).multiply(coeffiecients);
    }

    @Override
    public int getNumberOfVariables() {
        return NUMBER_OF_VARIABLES;
    }

    @Override
    public double getValue(RealMatrix point) {
        RealMatrix function = calculateFunction(point);
        double[] results = function.getColumn(0);
        return Arrays.stream(results).map(v -> v * v).sum();
    }

    @Override
    public RealMatrix gradient(RealMatrix point) {
        RealMatrix function = calculateFunction(point);
        return coeffiecients.transpose().scalarMultiply(2).multiply(function);
    }

    private RealMatrix calculateFunction(RealMatrix point) {
        return coeffiecients.multiply(point.transpose()).subtract(b);
    }

}
