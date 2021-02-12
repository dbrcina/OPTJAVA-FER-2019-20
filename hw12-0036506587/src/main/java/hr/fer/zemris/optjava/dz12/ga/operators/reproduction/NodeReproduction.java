package hr.fer.zemris.optjava.dz12.ga.operators.reproduction;

import hr.fer.zemris.optjava.dz12.ga.operators.selection.NTournamentSelection;
import hr.fer.zemris.optjava.dz12.ga.solution.Solution;

import java.util.List;

public class NodeReproduction {

    private NTournamentSelection selection;

    public NodeReproduction(NTournamentSelection selection) {
        this.selection = selection;
    }

    public Solution reproduce(List<Solution> population) {
        return new Solution(selection.select(population).getRoot().copy());
    }

}
