package hr.fer.zemris.optjava.dz8.algorithm;

import java.util.Random;

public class Initializer {

    /**
     * Uniformly initializes <i>matrix</i> with numbers between [a,b] for every row.
     *
     * @param rand   instance of {@link Random}.
     * @param a      param a.
     * @param b      param b.
     * @param matrix matrix.
     */
    public static void initialize(Random rand, double a, double b, double[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                matrix[i][j] = a + rand.nextDouble() * (b - a);
            }
        }
    }

}
