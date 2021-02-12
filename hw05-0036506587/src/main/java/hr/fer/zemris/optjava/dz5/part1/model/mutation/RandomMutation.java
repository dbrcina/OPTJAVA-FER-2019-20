package hr.fer.zemris.optjava.dz5.part1.model.mutation;

import hr.fer.zemris.optjava.dz5.part1.model.BitVector;

import java.util.Random;

public class RandomMutation implements IMutation<BitVector> {

    @Override
    public void mutate(Random rand, BitVector child) {
        for (int i = 0; i < child.size(); i++) {
            if (rand.nextDouble() <= 0.2) {
                child.set(i, !child.get(i));
            }
        }
    }
}
