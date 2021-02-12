package hr.fer.zemris.optjava.dz5.part2.model.crossover;

public interface ICrossover<T> {

    T crossover(T parent1, T parent2);

}
