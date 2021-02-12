package hr.fer.zemris.optjava.dz5.part1.model.mutation;

import hr.fer.zemris.optjava.dz5.part1.model.BitVector;

import java.util.Random;

public interface IMutation<T> {

    void mutate(Random rand, BitVector child);
}
