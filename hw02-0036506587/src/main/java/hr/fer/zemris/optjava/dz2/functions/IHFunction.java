package hr.fer.zemris.optjava.dz2.functions;

import org.apache.commons.math3.linear.RealMatrix;

/**
 * Interface which extends {@link IFunction} interface and provides method for generating
 * <i>Hesse-Matrix</i>.
 */
public interface IHFunction extends IFunction {

    /**
     * Generates <i>Hesse-Matrix</i> of provided <i>point</i>.
     *
     * @param point matrix.
     * @return <i>Hesse-Matrix</i>.
     */
    RealMatrix hesse(RealMatrix point);
}
