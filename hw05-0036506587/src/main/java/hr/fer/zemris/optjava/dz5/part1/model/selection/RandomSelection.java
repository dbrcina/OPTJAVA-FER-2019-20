package hr.fer.zemris.optjava.dz5.part1.model.selection;

import hr.fer.zemris.optjava.dz5.part1.model.BitVector;

import java.util.List;
import java.util.Random;

public class RandomSelection implements ISelection<BitVector> {

    @Override
    public BitVector selection(Random rand, List<BitVector> population) {
        return population.get(rand.nextInt(population.size()));
    }
}
