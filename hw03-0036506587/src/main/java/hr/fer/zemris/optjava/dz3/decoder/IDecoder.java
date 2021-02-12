package hr.fer.zemris.optjava.dz3.decoder;

import hr.fer.zemris.optjava.dz3.solution.SingleObjectiveSolution;

/**
 * Model of a simple decoder which is used to decode solutions into an array of decimal
 * numbers.
 *
 * @param <T> type of solution that extends {@link SingleObjectiveSolution}.
 */
public interface IDecoder<T extends SingleObjectiveSolution> {

    /**
     * @param solution solution.
     * @return representation of <i>solution</i> as an array of doubles.
     */
    double[] decode(T solution);

    /**
     * Fills out provided <i>values</i> array with decoded data from provided <i>solution</i>.
     *
     * @param solution solution.
     * @param values   values array.
     */
    void decode(T solution, double[] values);
}
