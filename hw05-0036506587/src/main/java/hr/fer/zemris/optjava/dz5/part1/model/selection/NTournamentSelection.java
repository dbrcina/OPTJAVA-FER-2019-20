package hr.fer.zemris.optjava.dz5.part1.model.selection;

import hr.fer.zemris.optjava.dz5.part1.algorithm.RAPGA;
import hr.fer.zemris.optjava.dz5.part1.model.BitVector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NTournamentSelection implements ISelection<BitVector> {

    private int n;

    public NTournamentSelection(int n) {
        this.n = n;
    }

    @Override
    public BitVector selection(Random rand, List<BitVector> population) {
        int size = population.size();
        List<BitVector> selectedUnits = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            selectedUnits.add(population.get(rand.nextInt(size)));
        }
        selectedUnits.sort((u1, u2) ->
                Double.compare(RAPGA.function.valueAt(u2), RAPGA.function.valueAt(u1)));
        return selectedUnits.get(0);
    }
}
