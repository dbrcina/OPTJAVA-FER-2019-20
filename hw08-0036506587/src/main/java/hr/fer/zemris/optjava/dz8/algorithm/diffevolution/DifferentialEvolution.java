package hr.fer.zemris.optjava.dz8.algorithm.diffevolution;

import hr.fer.zemris.optjava.dz8.algorithm.Initializer;
import hr.fer.zemris.optjava.dz8.algorithm.OptAlgorithm;
import hr.fer.zemris.optjava.dz8.algorithm.diffevolution.crossover.BinaryCrossover;
import hr.fer.zemris.optjava.dz8.algorithm.diffevolution.crossover.DifferentialCrossover;
import hr.fer.zemris.optjava.dz8.algorithm.diffevolution.mutation.BestMutation;
import hr.fer.zemris.optjava.dz8.algorithm.diffevolution.mutation.DifferentialMutation;
import hr.fer.zemris.optjava.dz8.algorithm.diffevolution.mutation.RandMutation;
import hr.fer.zemris.optjava.dz8.algorithm.diffevolution.selection.DifferentialSelection;
import hr.fer.zemris.optjava.dz8.neuralnetwork.NeuralNetwork;

import java.util.EnumSet;

/**
 * An implementation of {@link OptAlgorithm} class.
 */
public class DifferentialEvolution extends OptAlgorithm {

    private static final EnumSet<Strategy> VALID_STRATEGIES = EnumSet.allOf(Strategy.class);

    private Strategy strategy;
    private double[][] population;
    private double[] solution;

    private DifferentialMutation mutation;
    private DifferentialCrossover crossover;
    private DifferentialSelection selection;

    private final double F;
    private final double CR;

    /**
     * @param strategy strategy.
     * @param F        constant used for mutations.
     * @param CR       constant used for crossover.
     * @see OptAlgorithm#OptAlgorithm(NeuralNetwork, int, double, int, double, double)
     */
    public DifferentialEvolution(
            NeuralNetwork network,
            int popSize,
            double merr,
            int maxiter,
            double A,
            double B,
            String strategy,
            double F,
            double CR) {
        super(network, popSize, merr, maxiter, A, B);
        this.population = new double[popSize][network.getWeightsCount()];
        this.F = F;
        this.CR = CR;
        validateStrategy(strategy);
        initMutation();
        initCrossover();
        initSelection();
    }

    /**
     * Validates provided <i>strategy</i>.
     *
     * @param strategy strategy to validate.
     */
    private void validateStrategy(String strategy) {
        if (strategy == null) {
            this.strategy = Strategy.BEST_BIN;
            this.strategy.setDefinition("best/1/bin");
            return;
        }
        for (Strategy s : VALID_STRATEGIES) {
            if (strategy.matches(s.getDefinition())) {
                this.strategy = s;
                this.strategy.setDefinition(strategy);
                break;
            }
        }
        if (this.strategy == null)
            throw new IllegalArgumentException("Strategija '" + strategy + "' je nevaljana");
    }

    /**
     * Initializes mutation object.
     */
    private void initMutation() {
        String[] definitionParts = strategy.getDefinition().split("/");
        int a = Integer.parseInt(definitionParts[1]);
        if (definitionParts[0].equals("rand")) {
            mutation = new RandMutation(getRand(), F, a);
        } else {
            mutation = new BestMutation(getRand(), F, getNetwork(), a);
        }
    }

    /**
     * Initializes crossover object.
     */
    private void initCrossover() {
        if (strategy.getDefinition().endsWith("/bin")) {
            crossover = new BinaryCrossover(getRand(), CR);
        }
    }

    /**
     * Initializes selection object.
     */
    private void initSelection() {
        selection = new DifferentialSelection(getNetwork());
    }

    @Override
    public double[] run() {
        Initializer.initialize(getRand(), getA(), getB(), population);

        int maxiter = getMaxiter();
        double minError = Double.MAX_VALUE;

        for (int i = 0; i < maxiter; i++) {
            // evaluate population
            // if appropriate solution is found, break the main loop
            boolean found = false;
            for (double[] currentSolution : population) {
                double error = Util.evaluate(getNetwork(), currentSolution);
                if (error <= minError) {
                    setSolution(currentSolution);
                    minError = error;
                }
                if (minError <= getMerr()) {
                    found = true;
                    break;
                }
            }
            System.out.println((i + 1) + ". iteracija: " + minError);
            if (found) break;

            // go through every unit in population
            for (int j = 0; j < getPopSize(); j++) {
                double[] targetSolution = population[j];
                double[] mutantSolution = mutation.mutate(population);
                double[] trialSolution = crossover.crossover(targetSolution, mutantSolution);
                double[] selectedUnit = selection.select(trialSolution, targetSolution);
                setUnitInPopulation(selectedUnit, j);
            }
        }

        return solution;
    }

    protected void setUnitInPopulation(double[] unit, int index) {
        population[index] = unit.clone();
    }

    protected void setSolution(double[] solution) {
        this.solution = solution.clone();
    }

}
