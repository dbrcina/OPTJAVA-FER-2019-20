package hr.fer.zemris.optjava.dz3.neighborhood;

import hr.fer.zemris.optjava.dz3.solution.SingleObjectiveSolution;

/**
 * Model of a simple neighborhood solution generator.
 *
 * @param <T> type of solution that extends {@link SingleObjectiveSolution}.
 */
public interface INeighborhood<T extends SingleObjectiveSolution> {

    /**
     * Generates random neighbor for provided <i>solution</i>.
     *
     * @param solution solution.
     * @return solution's neighbor.
     */
    T randomNeighbor(T solution);
}
