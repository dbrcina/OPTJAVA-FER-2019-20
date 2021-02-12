package hr.fer.zemris.optjava.dz3.neighborhood;

import hr.fer.zemris.optjava.dz3.solution.BitvectorSolution;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * An implementation of {@link INeighborhood} interface.
 */
public class BitvectorNeighborhood implements INeighborhood<BitvectorSolution> {

    private static final Random RAND = new Random();

    @Override
    public BitvectorSolution randomNeighbor(BitvectorSolution solution) {
        int indexToChange = RAND.nextInt(solution.bits.length);
        boolean valueToChange = solution.bits[indexToChange];
        BitvectorSolution neighbor = solution.duplicate();
        neighbor.bits[indexToChange] = !valueToChange;
        return neighbor;
    }
}
