package hr.fer.zemris.optjava.dz12.ga.operators.selection;

import hr.fer.zemris.optjava.dz12.ga.solution.Solution;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NTournamentSelection {

    private Random rand;
    private int n;

    public NTournamentSelection(Random rand, int n) {
        this.rand = rand;
        this.n = n;
    }

    public Solution select(List<Solution> population) {
        List<Integer> usedIndexes = new ArrayList<>();
        List<Solution> selectedUnits = new ArrayList<>();
        while (selectedUnits.size() < n) {
            int index = rand.nextInt(population.size());
            if (usedIndexes.contains(index)) continue;
            usedIndexes.add(index);
            selectedUnits.add(population.get(index));
        }
        return selectedUnits.stream().sorted().findFirst().get();
    }

}
