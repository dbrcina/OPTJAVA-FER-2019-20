package hr.fer.zemris.optjava.dz10.selection;

import hr.fer.zemris.optjava.dz10.solution.Solution;

import java.util.List;
import java.util.Random;

public class CrowdedTournamentSelection {

    public static Solution select(Random rand, List<Solution> population) {
        int index1 = rand.nextInt(population.size());
        int index2 = index1;
        while (index2 == index1) index2 = rand.nextInt(population.size());

        Solution p1 = population.get(index1);
        Solution p2 = population.get(index2);

        int r = p1.compareTo(p2);
        return r < 0 ? p2 : p1;
    }

}
