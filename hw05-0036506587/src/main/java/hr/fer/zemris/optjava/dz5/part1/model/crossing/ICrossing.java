package hr.fer.zemris.optjava.dz5.part1.model.crossing;

import hr.fer.zemris.optjava.dz5.part1.model.BitVector;

import java.util.Random;

public interface ICrossing<T> {

    T crossover(Random rand, BitVector parent1, BitVector parent2);

}
