package hr.fer.zemris.optjava.dz8.algorithm.diffevolution;

import hr.fer.zemris.optjava.dz8.function.ErrorFunction;
import hr.fer.zemris.optjava.dz8.neuralnetwork.NeuralNetwork;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Utility class.
 */
public class Util {

    /**
     * Finds the best unit from <i>population</i> based on {@link ErrorFunction#calculateError(NeuralNetwork,
     * double[])} method and returns its index in population.
     *
     * @param network    neural network.
     * @param population population.
     * @return index of the best unit in population.
     */
    public static int getBestUnitIndex(NeuralNetwork network, double[][] population) {
        int length = population.length;
        double min = Double.MAX_VALUE;
        int bestUnitIndex = 0;
        for (int i = 0; i < length; i++) {
            double[] unit = population[i];
            double error = ErrorFunction.calculateError(network, unit);
            if (error <= min) {
                min = error;
                bestUnitIndex = i;
            }
        }
        return bestUnitIndex;
    }

    /**
     * Finds <i>a * 2</i> different random units from <i>population</i> and returns their
     * indexes in population.<br>
     * <i>bestUnitIndex</i> is excluded from search.
     *
     * @param rand          an instance of {@link Random}.
     * @param population    population.
     * @param a             number of differences.
     * @param bestUnitIndex best units index.
     * @return indexes of random units.
     */
    public static int[] getRandomUnitIndexes(
            Random rand,
            double[][] population,
            int a,
            int bestUnitIndex) {
        int length = population.length;
        int numberOfUnits = a * 2;
        List<Integer> usedIndexes = new ArrayList<>();
        usedIndexes.add(bestUnitIndex);
        int[] indexes = new int[numberOfUnits];
        for (int i = 0; i < numberOfUnits; i++) {
            int index = rand.nextInt(length);
            // find unique index
            while (usedIndexes.contains(index)) {
                index = rand.nextInt(length);
            }
            indexes[i] = index;
            usedIndexes.add(index);
        }
        return indexes;
    }

    /**
     * Evaluates <i>network</i> for provided <i>solution</i> based on {@link
     * ErrorFunction#calculateError(NeuralNetwork, double[])} method.
     *
     * @param network  network.
     * @param solution solution.
     * @return calculated error.
     */
    public static double evaluate(NeuralNetwork network, double[] solution) {
        return ErrorFunction.calculateError(network, solution);
    }

}
