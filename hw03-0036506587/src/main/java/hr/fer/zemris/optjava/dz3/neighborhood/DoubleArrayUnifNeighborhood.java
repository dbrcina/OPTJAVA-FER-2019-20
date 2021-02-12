package hr.fer.zemris.optjava.dz3.neighborhood;

import hr.fer.zemris.optjava.dz3.solution.DoubleArraySolution;

import java.util.Arrays;
import java.util.Random;

/**
 * An implementation of {@link INeighborhood} interface which provides uniform neighborhood.
 */
public class DoubleArrayUnifNeighborhood implements INeighborhood<DoubleArraySolution> {

    private static final Random rand = new Random();
    private double[] deltas;

    /**
     * Constructor.
     *
     * @param deltas deltas.
     */
    public DoubleArrayUnifNeighborhood(double[] deltas) {
        this.deltas = deltas;
    }

    @Override
    public DoubleArraySolution randomNeighbor(DoubleArraySolution solution) {
        DoubleArraySolution neighbor = solution.duplicate();
        neighbor.randomize(rand, Arrays.stream(deltas).map(d -> d * -1).toArray(), deltas);
        return neighbor;
    }
}
