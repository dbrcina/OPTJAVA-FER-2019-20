package hr.fer.zemris.optjava.dz2.functions;

import org.apache.commons.math3.linear.RealMatrix;

/**
 * Interface of scalar function over <i><b>n</b></i> dimensional vector of real numbers.
 */
public interface IFunction {

    /**
     * @return number of variables.
     */
    int getNumberOfVariables();

    /**
     * @param point point.
     * @return function value at <i>point</i> point.
     */
    double getValue(RealMatrix point);

    /**
     * @param point point.
     * @return function gradient at <i>point</i> point.
     */
    RealMatrix gradient(RealMatrix point);
}
