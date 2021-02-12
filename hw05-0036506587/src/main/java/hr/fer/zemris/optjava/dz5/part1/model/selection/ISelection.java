package hr.fer.zemris.optjava.dz5.part1.model.selection;

import hr.fer.zemris.optjava.dz5.part1.model.BitVector;
import hr.fer.zemris.optjava.dz5.part1.model.Population;

import java.util.List;
import java.util.Random;

public interface ISelection<T> {

    T selection(Random rand, List<BitVector> population);
}
