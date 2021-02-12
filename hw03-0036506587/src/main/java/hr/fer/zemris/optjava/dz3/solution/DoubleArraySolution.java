package hr.fer.zemris.optjava.dz3.solution;

import java.util.Random;

/**
 * An implementation of {@link SingleObjectiveSolution} which represents solution as an array
 * of doubles.
 */
public class DoubleArraySolution extends SingleObjectiveSolution {

    public double[] values;

    /**
     * Constructor.
     *
     * @param size size.
     */
    public DoubleArraySolution(int size) {
        this.values = new double[size];
    }

    /**
     * @return new instance of {@link DoubleArraySolution}.
     */
    public DoubleArraySolution newLikeThis() {
        return new DoubleArraySolution(values.length);
    }

    /**
     * @return new instance of {@link DoubleArraySolution} with the same values as
     * <i><b>this</b></i>.
     */
    public DoubleArraySolution duplicate() {
        DoubleArraySolution duplicated = newLikeThis();
        System.arraycopy(values, 0, duplicated.values, 0, values.length);
        return duplicated;
    }

    /**
     * Fills out <i><b>this</b></i> solution with pseudo random doubles within bonds provided
     * as arrays of doubles(<i>min,max</i>).
     *
     * @param rand instance of {@link Random}.
     * @param min  array of minimum values.
     * @param max  array of maximum values.
     */
    public void randomize(Random rand, double[] min, double[] max) {
        for (int i = 0; i < values.length; i++) {
            values[i] += min[i] + rand.nextDouble() * (max[i] - min[i]);
        }
    }
}
