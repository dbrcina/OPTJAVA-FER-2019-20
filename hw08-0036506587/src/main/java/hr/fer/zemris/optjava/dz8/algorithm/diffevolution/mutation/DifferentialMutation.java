package hr.fer.zemris.optjava.dz8.algorithm.diffevolution.mutation;

import java.util.Random;

/**
 * An implementation of {@link Mutation} interface.
 */
public abstract class DifferentialMutation implements Mutation<double[]> {

    private final Random rand;
    private final double F;

    public DifferentialMutation(Random rand, double F) {
        this.rand = rand;
        this.F = F;
    }

    protected Random getRand() {
        return rand;
    }

    protected double getF() {
        return F;
    }

}
