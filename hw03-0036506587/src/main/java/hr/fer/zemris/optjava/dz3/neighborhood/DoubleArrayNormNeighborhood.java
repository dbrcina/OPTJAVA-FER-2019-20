package hr.fer.zemris.optjava.dz3.neighborhood;

import hr.fer.zemris.optjava.dz3.solution.DoubleArraySolution;

import java.util.Arrays;
import java.util.Random;

/**
 * An implementation of {@link INeighborhood} interface which provides <i>Gaussian</i>
 * neighborhood.
 */
public class DoubleArrayNormNeighborhood implements INeighborhood<DoubleArraySolution> {

    private static final Random rand = new Random();
    private double[] deltas;

    /**
     * Constructor.
     *
     * @param deltas deltas.
     */
    public DoubleArrayNormNeighborhood(double[] deltas) {
        this.deltas = deltas;
    }

    @Override
    public DoubleArraySolution randomNeighbor(DoubleArraySolution solution) {
        DoubleArraySolution neighbor = solution.duplicate();
        double[] bounds = Arrays.stream(deltas).map(d -> rand.nextGaussian() * d).toArray();
        neighbor.randomize(rand, Arrays.stream(bounds).map(d -> d * -1).toArray(), bounds);
        return neighbor;
    }
}
