package hr.fer.zemris.optjava.dz10.crossover;

import hr.fer.zemris.optjava.dz10.solution.Solution;

import java.util.Random;

public class UniformCrossover {

    public static double[] crossover(Random rand, Solution[] parents) {
        double[] p1Values = parents[0].getValues();
        double[] p2Values = parents[1].getValues();
        int length = p1Values.length;
        double[] childValues = new double[length];
        for (int i = 0; i < length; i++) {
            childValues[i] = rand.nextDouble() < 0.5 ? p1Values[i] : p2Values[i];
        }
        return childValues;
    }

}
