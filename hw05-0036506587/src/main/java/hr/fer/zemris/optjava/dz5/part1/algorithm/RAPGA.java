package hr.fer.zemris.optjava.dz5.part1.algorithm;

import hr.fer.zemris.optjava.dz5.part1.function.FunctionImpl;
import hr.fer.zemris.optjava.dz5.part1.function.IFunction;
import hr.fer.zemris.optjava.dz5.part1.model.BitVector;
import hr.fer.zemris.optjava.dz5.part1.model.crossing.ICrossing;
import hr.fer.zemris.optjava.dz5.part1.model.crossing.RandomCrossing;
import hr.fer.zemris.optjava.dz5.part1.model.mutation.IMutation;
import hr.fer.zemris.optjava.dz5.part1.model.mutation.RandomMutation;
import hr.fer.zemris.optjava.dz5.part1.model.selection.ISelection;
import hr.fer.zemris.optjava.dz5.part1.model.selection.NTournamentSelection;
import hr.fer.zemris.optjava.dz5.part1.model.selection.RandomSelection;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RAPGA {

    public static final IFunction<BitVector> function = new FunctionImpl();

    private int minPop;
    private int maxPop;
    private int maxEffort;
    private int n;
    private int iterations;
    private Random rand = new Random();
    private ISelection<BitVector> nTournamentSelection = new NTournamentSelection(2);
    private ISelection<BitVector> randomSelection = new RandomSelection();
    private ICrossing<BitVector> randomCrossing = new RandomCrossing();
    private IMutation<BitVector> randomMutation = new RandomMutation();

    public RAPGA(int minPop, int maxPop, int maxEffort, int n, int iterations) {
        this.minPop = minPop;
        this.maxPop = maxPop;
        this.maxEffort = maxEffort;
        this.n = n;
        this.iterations = iterations;
    }

    public List<BitVector> run() {
        List<BitVector> initialPopulation = initialPopulation();
        double compFactor = 0;
        int i = 0;
        while (i < iterations && initialPopulation.size() >= minPop) {
            List<BitVector> tempPopulation = new ArrayList<>();
            int childCount = 0;
            while (childCount < maxEffort && tempPopulation.size() < maxPop) {
                BitVector parent1 = nTournamentSelection.selection(rand, initialPopulation);
                BitVector parent2 = nTournamentSelection.selection(rand, initialPopulation);
                //BitVector parent2 = randomSelection.selection(rand, initialPopulation);
                BitVector child = randomCrossing.crossover(rand, parent1, parent2);
                randomMutation.mutate(rand, child);
                childCount++;
                double goriRoditelj = function.valueAt(parent1);
                double boljiRoditelj = function.valueAt(parent2);
                if (goriRoditelj > boljiRoditelj) {
                    double temp = goriRoditelj;
                    goriRoditelj = boljiRoditelj;
                    boljiRoditelj = temp;
                }
                double fitPrag = goriRoditelj + compFactor * (boljiRoditelj - goriRoditelj);
                double fitDijete = function.valueAt(child);
                if (fitDijete > fitPrag) {
                    tempPopulation.add(child);
                }
                compFactor += 1.0 / iterations;
            }
            if (!tempPopulation.isEmpty())
                initialPopulation = tempPopulation;
            i--;
            initialPopulation.sort((b1, b2) -> Double.compare(function.valueAt(b2),
                    function.valueAt(b1)));
            System.out.println(initialPopulation.get(0));
        }
        return initialPopulation;
    }

    private List<BitVector> initialPopulation() {
        List<BitVector> population = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            BitVector unit = new BitVector(n);
            unit.randomize(rand);
            population.add(unit);
        }
        return population;
    }
}
