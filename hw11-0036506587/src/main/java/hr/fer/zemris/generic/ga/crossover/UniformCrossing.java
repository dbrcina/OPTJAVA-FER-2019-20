package hr.fer.zemris.generic.ga.crossover;

import hr.fer.zemris.generic.ga.solution.IntArraySolution;
import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.RNG;

public class UniformCrossing {

    private final IRNG rng = RNG.getRNG();

    public IntArraySolution crossover(IntArraySolution[] parents) {
        int[] p1Data = parents[0].getData();
        int[] p2Data = parents[1].getData();
        int dataLength = p1Data.length;
        int[] childData = new int[dataLength];

        for (int i = 0; i < dataLength; i++) {
            childData[i] = rng.nextDouble() < 0.5 ? p1Data[i] : p2Data[i];
        }

        return new IntArraySolution(childData);
    }

}
