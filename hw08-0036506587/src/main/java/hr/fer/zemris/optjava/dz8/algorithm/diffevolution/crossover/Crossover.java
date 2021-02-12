package hr.fer.zemris.optjava.dz8.algorithm.diffevolution.crossover;

/**
 * Simple functional interface which provides {@link #crossover(Object, Object)} method.
 *
 * @param <T> type T.
 */
@FunctionalInterface
public interface Crossover<T> {

    /**
     * Does crossover between <i>t1</i> and <i>t2</i>.
     *
     * @param t1 t1 unit.
     * @param t2 t2 unit.
     * @return crossover result.
     */
    T crossover(T t1, T t2);

}
