package hr.fer.zemris.optjava.dz2.functions;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

/**
 * <pre><b>f1(x1,x2)=(x1-1)^2+10*(x2-2)^2</b></pre>
 * <p>
 * An implementation of {@link IHFunction} interface.<br> Every method is hardcoded based on
 * mentioned function.</p>
 */
public class Function2 implements IHFunction {

    private static final int NUMBER_OF_VARIABLES = 2;

    @Override
    public RealMatrix hesse(RealMatrix point) {
        return new Array2DRowRealMatrix(new double[][]{
                {2, 0},
                {0, 20}
        });
    }

    @Override
    public int getNumberOfVariables() {
        return NUMBER_OF_VARIABLES;
    }

    @Override
    public double getValue(RealMatrix point) {
        double[] values = point.getRow(0);
        return Math.pow(values[0] - 1, 2) + 10 * Math.pow(values[1] - 2, 2);
    }

    @Override
    public RealMatrix gradient(RealMatrix point) {
        double[] values = point.getRow(0);
        double[][] gradient = new double[][]{
                {2 * (values[0] - 1)},
                {20 * (values[1] - 2)}
        };
        return new Array2DRowRealMatrix(gradient);
    }
}
