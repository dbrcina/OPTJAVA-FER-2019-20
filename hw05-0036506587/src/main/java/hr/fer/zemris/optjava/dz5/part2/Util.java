package hr.fer.zemris.optjava.dz5.part2;

import java.util.Random;

public class Util {

    public static double[] shuffleArray(double[] array) {
        Random rnd = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            double a = array[index];
            array[index] = array[i];
            array[i] = a;
        }
        return array;
    }

    public static int[] shuffleArray(int[] array) {
        Random rnd = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            int a = array[index];
            array[index] = array[i];
            array[i] = a;
        }
        return array;
    }

}
