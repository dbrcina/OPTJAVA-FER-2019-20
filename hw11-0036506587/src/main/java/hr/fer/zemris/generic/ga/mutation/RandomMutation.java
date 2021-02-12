package hr.fer.zemris.generic.ga.mutation;

import hr.fer.zemris.generic.ga.solution.IntArraySolution;
import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.RNG;

public class RandomMutation {

    private final IRNG rng = RNG.getRNG();

    public void mutate(IntArraySolution unit, int width, int height) {
        int[] data = unit.getData();
        for (int i = 0; i < data.length; i++) {
            if (rng.nextDouble() < 0.02) {
                if (i % 5 == 0) {
                    data[i] = rng.nextInt(Byte.MIN_VALUE, Byte.MAX_VALUE + 1);
                }
                if (i % 5 == 1) {
                    data[i] = rng.nextInt(0, width);
                }
                if (i % 5 == 2) {
                    data[i] = rng.nextInt(0, height);
                }
                if (i % 5 == 3) {
                    data[i] = rng.nextInt(1, width);
                }
                if (i % 5 == 4) {
                    data[i] = rng.nextInt(1, height);
                }
            }
        }
    }

}
