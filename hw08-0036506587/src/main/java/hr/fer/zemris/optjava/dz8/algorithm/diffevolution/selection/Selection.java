package hr.fer.zemris.optjava.dz8.algorithm.diffevolution.selection;

/**
 * Simple functional interface which provides {@link #select(Object, Object)} method.
 *
 * @param <T> type t.
 */
@FunctionalInterface
public interface Selection<T> {

    /**
     * Selects better object between <i>t1</i> and <i>t2</i>.
     *
     * @param t1 parameter t1.
     * @param t2 parameter t2.
     * @return selected object.
     */
    T select(T t1, T t2);

}
