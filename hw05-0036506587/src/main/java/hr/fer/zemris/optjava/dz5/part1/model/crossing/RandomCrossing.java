package hr.fer.zemris.optjava.dz5.part1.model.crossing;

import hr.fer.zemris.optjava.dz5.part1.model.BitVector;

import java.util.Random;

public class RandomCrossing implements ICrossing<BitVector> {

    @Override
    public BitVector crossover(Random rand, BitVector parent1, BitVector parent2) {
        BitVector child = new BitVector(parent1.size());
        for (int i = 0; i < child.size(); i++) {
            child.set(i, rand.nextDouble() <= 0.5 ? parent1.get(i) : parent2.get(i));
        }
        return child;
    }
}
