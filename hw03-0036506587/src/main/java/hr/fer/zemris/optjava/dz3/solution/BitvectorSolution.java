package hr.fer.zemris.optjava.dz3.solution;

import java.util.Random;

/**
 * An implementation of {@link SingleObjectiveSolution} which represents solution as an array
 * of bits.
 */
public class BitvectorSolution extends SingleObjectiveSolution {

    public boolean[] bits;

    /**
     * Constructor.
     *
     * @param size size.
     */
    public BitvectorSolution(int size) {
        this.bits = new boolean[size];
    }

    /**
     * @return new instance of {@link DoubleArraySolution}.
     */
    public BitvectorSolution newLikeThis() {
        return new BitvectorSolution(bits.length);
    }

    /**
     * @return new instance of {@link DoubleArraySolution} with the same values as
     * <i><b>this</b></i>.
     */
    public BitvectorSolution duplicate() {
        BitvectorSolution duplicated = newLikeThis();
        System.arraycopy(bits, 0, duplicated.bits, 0, bits.length);
        return duplicated;
    }

    /**
     * Fills out <i><b>this</b></i> solution with pseudo random bits.
     *
     * @param rand rand.
     */
    public void randomize(Random rand) {
        for (int i = 0; i < bits.length; i++) {
            bits[i] = rand.nextBoolean();
        }
    }

}
