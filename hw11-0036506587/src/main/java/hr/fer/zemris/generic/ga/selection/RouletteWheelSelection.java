package hr.fer.zemris.generic.ga.selection;

import hr.fer.zemris.generic.ga.solution.IntArraySolution;
import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.RNG;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RouletteWheelSelection {

    private final IRNG rng = RNG.getRNG();

    public IntArraySolution[] select(List<IntArraySolution> population) {
        final int limit = population.size() / 10;
        List<IntArraySolution> limited = population.stream()
                .limit(limit)
                .collect(Collectors.toCollection(ArrayList::new));

        double sum = limited.stream().mapToDouble(s -> s.fitness).sum();
        double previousProbability = 0.0;
        double[] percentages = new double[limit];
        for (int i = limit - 1; i > -1; i--) {
            percentages[i] = previousProbability + limited.get(i).fitness / sum;
            previousProbability = percentages[i];
        }

        IntArraySolution[] parents = new IntArraySolution[2];
        for (int i = 0; i < parents.length; i++) {
            double random = rng.nextDouble();
            for (int j = 0; j < limit; j++) {
                if (random < percentages[j]) {
                    parents[i] = limited.get(j);
                    break;
                }
            }
        }

        return parents;
    }

}
