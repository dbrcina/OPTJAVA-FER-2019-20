package hr.fer.zemris.optjava.dz4.part2.algorithm;

import hr.fer.zemris.optjava.dz4.part2.model.Population;
import hr.fer.zemris.optjava.dz4.part2.model.unit.Box;
import hr.fer.zemris.optjava.dz4.part2.model.crossover.CrossoverOperation;
import hr.fer.zemris.optjava.dz4.part2.model.item.Stick;
import hr.fer.zemris.optjava.dz4.part2.model.mutation.MutationOperation;
import hr.fer.zemris.optjava.dz4.part2.model.selection.NTournamentSelection;

import java.util.List;
import java.util.Random;

/**
 * <i>Hybrid Grouping Genetic Algorithm.</i> <br> It's an example of steady-state genetic
 * algorithm.
 *
 * @see <a href="https://www.codeproject.com/Articles/633133/ga-bin-packing">
 * https://www.codeproject.com/Articles/633133/ga-bin-packing</a>
 */
public class HGGA {

    private static final int NUMBER_OF_PARENTS = 2;
    private int height;
    private int populationSize;
    private int n;
    private int m;
    private boolean p;
    private int iterations;
    private int s;
    private double sigma;
    private List<Stick> loadedSticks;
    private Random rand = new Random();

    public HGGA(
            int height,
            int populationSize,
            int n,
            int m,
            boolean p,
            int iterations,
            int s,
            double sigma,
            List<Stick> loadedSticks) {
        this.height = height;
        this.populationSize = populationSize;
        this.n = n;
        this.m = m;
        this.p = p;
        this.iterations = iterations;
        this.s = s;
        this.sigma = sigma;
        this.loadedSticks = loadedSticks;
    }

    public Box run() {
        Population population = new Population(populationSize);
        population.randomize(loadedSticks, height);
        for (int i = 0; i < iterations; i++) {
            if (evaluate(population)) return population.getBestBox();
            Box[] parents = new Box[NUMBER_OF_PARENTS];
            for (int j = 0; j < parents.length; j++) {
                parents[j] = NTournamentSelection.selection(rand, population, n, true);
            }
            Box child = CrossoverOperation.crossover(rand, parents);
            MutationOperation.mutate(rand, child, sigma);
            Box randomBox = NTournamentSelection.selection(rand, population, m, false);
            if (!p) population.replace(child, randomBox);
            else {
                if (randomBox.size() < child.size()) {
                    population.replace(child, randomBox);
                }
            }
            System.out.println(child.size());
        }
        return population.getBestBox();
    }

    private boolean evaluate(Population population) {
        return population.getBoxes()
                .stream()
                .anyMatch(b -> b.size() <= s);
    }
}
