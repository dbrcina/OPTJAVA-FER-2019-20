package hr.fer.zemris.optjava.dz4.part1.function;

/**
 * Model of generic function which provides {@link IFunction#valueAt(double[])} method.
 */
public interface IFunction {

    /**
     * Calculates value of function at <i>point</i>.
     *
     * @param point point.
     * @return calculated value.
     */
    double valueAt(double[] point);
}
