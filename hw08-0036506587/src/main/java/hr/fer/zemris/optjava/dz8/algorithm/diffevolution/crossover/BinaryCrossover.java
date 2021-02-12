package hr.fer.zemris.optjava.dz8.algorithm.diffevolution.crossover;

import java.util.Random;

/**
 * An implementation of {@link DifferentialCrossover} class.
 */
public class BinaryCrossover extends DifferentialCrossover {

    private final double CR;

    /**
     * Constructor.
     *
     * @param rand an instance of {@link Random}.
     * @param CR   represents probability for crossing.
     */
    public BinaryCrossover(Random rand, double CR) {
        super(rand);
        this.CR = CR;
    }

    @Override
    public double[] crossover(double[] t1, double[] t2) {
        Random rand = getRand();
        int length = t1.length;
        double[] result = new double[length];

        // copy one value from t2 to result directly
        int start = rand.nextInt(length);
        result[start] = t2[start];

        // go through other elements
        for (int i = 0; i < length; i++) {
            if (i == start) continue;
            if (rand.nextDouble() <= CR) result[i] = t2[i];
            else result[i] = t1[i];
        }

        return result;
    }

}
