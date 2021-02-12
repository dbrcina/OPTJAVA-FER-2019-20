package hr.fer.zemris.optjava.dz3.function;

/**
 * Model of generic function which povides {@link IFunction#valueAt(double[])} method.
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
