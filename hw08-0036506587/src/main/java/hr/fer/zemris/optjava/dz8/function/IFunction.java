package hr.fer.zemris.optjava.dz8.function;

/**
 * Simple functional interface which provides {@link #valueAt(double)} method.
 */
@FunctionalInterface
public interface IFunction {

    /**
     * Calculates functions value at provided point <i>x</i>.
     *
     * @param x point x.
     * @return value at point <i>x</i>.
     */
    double valueAt(double x);

}
