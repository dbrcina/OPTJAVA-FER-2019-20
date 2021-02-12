package hr.fer.zemris.optjava.dz9.mutation;

import java.util.Random;

public class GaussianMutation {

    public static void mutate(Random rand, double[] unit, double[] mins, double[] maxs) {
        for (int i = 0; i < unit.length; i++) {
            unit[i] += rand.nextGaussian();
            if (unit[i] < mins[i]) unit[i] = mins[i];
            if (unit[i] > maxs[i]) unit[i] = maxs[i];
        }
    }

}
