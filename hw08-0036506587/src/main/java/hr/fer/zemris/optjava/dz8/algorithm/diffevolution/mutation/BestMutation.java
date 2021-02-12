package hr.fer.zemris.optjava.dz8.algorithm.diffevolution.mutation;

import hr.fer.zemris.optjava.dz8.algorithm.diffevolution.Util;
import hr.fer.zemris.optjava.dz8.neuralnetwork.NeuralNetwork;

import java.util.Random;

/**
 * An implementation of {@link DifferentialMutation} class.<br> In this implementation, base
 * unit is the best unit of a population.
 */
public class BestMutation extends DifferentialMutation {

    private final NeuralNetwork network;
    private final int a;

    /**
     * Constructor.
     *
     * @param rand    instance of {@link Random}.
     * @param F       constant used for mutation.
     * @param network network used for finding best unit.
     * @param a       number of differences.
     */
    public BestMutation(Random rand, double F, NeuralNetwork network, int a) {
        super(rand, F);
        this.network = network;
        this.a = a;
    }

    @Override
    public double[] mutate(double[][] population) {
        Random rand = getRand();

        // find best base unit
        int baseUnitIndex = Util.getBestUnitIndex(network, population);
        double[] baseUnit = population[baseUnitIndex];

        // find a * 2 unit indexes
        int[] indexes = Util.getRandomUnitIndexes(rand, population, a, baseUnitIndex);

        // calculate vector mutant
        double F = getF();
        int length = population[0].length;
        double[] unitMutant = new double[length];
        for (int i = 0; i < length; i++) {
            unitMutant[i] = baseUnit[i];
            for (int j = 0; j < a * 2 - 1; j++) {
                // mutant = base[i] + F * (u1[i] - u2[i]) + F * (u3[i] - u4[i]) +....
                // it depends on parameter a
                double[] unitj = population[indexes[j]];
                double[] unitjj = population[indexes[j + 1]];
                unitMutant[i] += F * (unitj[i] - unitjj[i]);
            }
            // update F
            F += 0.001 * (rand.nextDouble() - 0.5);
        }

        return unitMutant;
    }

}
