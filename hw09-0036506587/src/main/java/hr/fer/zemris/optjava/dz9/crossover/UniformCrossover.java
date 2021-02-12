package hr.fer.zemris.optjava.dz9.crossover;

import java.util.Random;

public class UniformCrossover {

    public static double[] crossover(Random rand, double[] p1, double[] p2) {
        double[] child = new double[p1.length];
        for (int i = 0; i < p1.length; i++) {
            child[i] = rand.nextDouble() < 0.5 ? p1[i] : p2[i];
        }
        return child;
    }

}
