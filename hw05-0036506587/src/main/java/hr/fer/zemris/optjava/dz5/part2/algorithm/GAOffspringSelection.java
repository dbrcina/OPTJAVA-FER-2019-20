package hr.fer.zemris.optjava.dz5.part2.algorithm;

import hr.fer.zemris.optjava.dz5.part2.function.IFunction;
import hr.fer.zemris.optjava.dz5.part2.model.Pool;
import hr.fer.zemris.optjava.dz5.part2.model.Unit;
import hr.fer.zemris.optjava.dz5.part2.model.crossover.ICrossover;
import hr.fer.zemris.optjava.dz5.part2.model.crossover.RandomCrossover;
import hr.fer.zemris.optjava.dz5.part2.model.mutation.IMutation;
import hr.fer.zemris.optjava.dz5.part2.model.mutation.RandomMutation;

import java.util.*;

public class GAOffspringSelection {

    private final int iterations;
    private final double succRation = 0.9;
    private final double maxSelPress = 50;
    private final double compFactorLower = 0.3;
    private final double compFactorUpper = 1;
    private final IFunction<Unit> function;
    private final Random rand = new Random();
    private final ICrossover<Unit> crossover = new RandomCrossover(rand);
    private final IMutation<Unit> mutation = new RandomMutation(rand);

    public GAOffspringSelection(int iterations, IFunction<Unit> function) {
        this.iterations = iterations;
        this.function = function;
    }

    public List<Unit> run(List<Unit> population) {
        int i = 0;
        double compFactor = compFactorLower;
        double actSelPress = 1;
        Collections.sort(population);
        while (i < iterations && actSelPress < maxSelPress) {
            List<Unit> nextPopulation = new ArrayList<>();
            Pool pool = new Pool();
            int initialSize = population.size();
            while (nextPopulation.size() < initialSize * succRation
                    && nextPopulation.size() + pool.size() < initialSize * maxSelPress) {
                Unit[] parents = new Unit[2];
                for (int j = 0; j < 2; j++) {
                    parents[j] = population.get(rand.nextInt(initialSize));
                }
                Arrays.sort(parents);
                Unit child = crossover.crossover(parents[0], parents[1]);
                mutation.mutate(child);
                double fitP1 = function.valutAt(parents[0]);
                double fitP2 = function.valutAt(parents[1]);
                double fitC = function.valutAt(child);
                child.setFitness(fitC);
                if (fitC <= fitP2 + (fitP2 - fitP1) * compFactor) {
                    pool.addUnit(child);
                } else {
                    nextPopulation.add(child);
                }
            }
            actSelPress = (1.0 * nextPopulation.size() + pool.size()) / initialSize;
            Set<Unit> poolUnits = pool.getUnits();
            for (Unit poolUnit : poolUnits) {
                if (nextPopulation.size() == initialSize) break;
                nextPopulation.add(poolUnit);
            }
            compFactor += (compFactorUpper - compFactorLower) / iterations;
            i++;
            population = nextPopulation;
        }
        return population;
    }

}
