package hr.fer.zemris.optjava.dz8.algorithm.diffevolution.mutation;

import hr.fer.zemris.optjava.dz8.algorithm.diffevolution.Util;

import java.util.Random;

/**
 * An implementation of {@link DifferentialMutation} class.<br> In this implementation, base
 * unit is generated randomly from a population.
 */
public class RandMutation extends DifferentialMutation {

    private final int a;

    /**
     * Constructor.
     *
     * @param rand instance of {@link Random}
     * @param F    constant used for mutation.
     * @param a    number of differences.
     */
    public RandMutation(Random rand, double F, int a) {
        super(rand, F);
        this.a = a;
    }

    @Override
    public double[] mutate(double[][] population) {
        Random rand = getRand();
        int popSize = population.length;

        // find base unit
        int baseUnitIndex = rand.nextInt(popSize);
        double[] baseUnit = population[baseUnitIndex];

        // find a * 2 unit indexes
        int[] indexes = Util.getRandomUnitIndexes(rand, population, a, baseUnitIndex);

        // calculate unit mutant
        double F = getF();
        double[] unitMutant = new double[population[0].length]; // weights count..
        for (int i = 0; i < unitMutant.length; i++) {
            unitMutant[i] = baseUnit[i];
            for (int j = 0; j < a * 2 - 1; j++) {
                // mutant = base[i] + F * (u1[i] - u2[i]) + F * (u3[i] - u4[i]) +....
                // it depends on parameter a
                double[] unitj = population[indexes[j]];
                double[] unitjj = population[indexes[j + 1]];
                unitMutant[i] += F * (unitj[i] - unitjj[i]);
            }
        }

        return unitMutant;
    }

}
