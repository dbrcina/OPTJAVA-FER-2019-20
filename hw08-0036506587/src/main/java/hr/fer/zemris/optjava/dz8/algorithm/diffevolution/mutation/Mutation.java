package hr.fer.zemris.optjava.dz8.algorithm.diffevolution.mutation;

/**
 * Simple functional interface which provides {@link #mutate(Object[])} method.
 *
 * @param <T> type T.
 */
@FunctionalInterface
public interface Mutation<T> {

    /**
     * Takes some units from provided array <i>t</i> and does mutation.
     *
     * @param t an array.
     */
    T mutate(T[] t);

}
