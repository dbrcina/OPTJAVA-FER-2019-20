package hr.fer.zemris.optjava.dz3;

import hr.fer.zemris.optjava.dz3.solution.SingleObjectiveSolution;

/**
 * Simple model of functional interface which provides method run.
 *
 * @param <T> type of solution that extends {@link SingleObjectiveSolution}.
 */
@FunctionalInterface
public interface IOptAlgorithm<T extends SingleObjectiveSolution> {

    /**
     * Runs the algorithm and returns the solution.
     */
    T run();
}
