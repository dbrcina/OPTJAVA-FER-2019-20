package hr.fer.zemris.optjava.dz8.algorithm.diffevolution.crossover;

import java.util.Random;

/**
 * An implementation of {@link Crossover} interface.
 */
public abstract class DifferentialCrossover implements Crossover<double[]> {

    private final Random rand;

    public DifferentialCrossover(Random rand) {
        this.rand = rand;
    }

    protected Random getRand() {
        return rand;
    }

}
