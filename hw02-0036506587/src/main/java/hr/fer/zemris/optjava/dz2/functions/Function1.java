package hr.fer.zemris.optjava.dz2.functions;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

/**
 * <pre><b>f1(x1,x2)=x1^2+(x2-1)^2</b></pre>
 * <p>
 * An implementation of {@link IHFunction} interface.<br> Every method is hardcoded based on
 * mentioned function.</p>
 */
public class Function1 implements IHFunction {

    private static final int NUMBER_OF_VARIABLES = 2;

    @Override
    public RealMatrix hesse(RealMatrix point) {
        return new Array2DRowRealMatrix(new double[][]{
                {2, 0},
                {0, 2}
        });
    }

    @Override
    public int getNumberOfVariables() {
        return NUMBER_OF_VARIABLES;
    }

    @Override
    public double getValue(RealMatrix point) {
        double[] values = point.getRow(0);
        return Math.pow(values[0], 2) + Math.pow(values[1] - 1, 2);
    }

    @Override
    public RealMatrix gradient(RealMatrix point) {
        double[] values = point.getRow(0);
        double[][] gradient = new double[][]{
                {2 * values[0]},
                {2 * (values[1] - 1)}
        };
        return new Array2DRowRealMatrix(gradient);
    }
}
