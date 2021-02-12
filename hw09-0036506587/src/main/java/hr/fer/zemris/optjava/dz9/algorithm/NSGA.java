package hr.fer.zemris.optjava.dz9.algorithm;

import hr.fer.zemris.optjava.dz9.crossover.UniformCrossover;
import hr.fer.zemris.optjava.dz9.mutation.GaussianMutation;
import hr.fer.zemris.optjava.dz9.problem.MOOPProblem;
import hr.fer.zemris.optjava.dz9.selection.RoulleteWheelSelection;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class NSGA {

    private final MOOPProblem problem;
    private final int velpop;
    private final String vrsta;
    private final int maxiter;

    private final Random rand = new Random();

    private double[][] population;
    private double[][] evaluatedValues;
    private double fitnesses[];

    private static final double SIGMA_SHARE = 1.0;
    private static final double ALPHA = 2.0;
    private static final double EPSILON = 0.99;

    public NSGA(MOOPProblem problem, int velpop, String vrsta, int maxiter) {
        this.problem = problem;
        this.velpop = velpop;
        this.vrsta = vrsta;
        this.maxiter = maxiter;
    }

    public void run() {
        final int numberOfObjectives = problem.getNumberOfObjectives();
        final double[] minArgumentBonds = problem.getMinArgumentBonds();
        final double[] maxArgumentBonds = problem.getMaxArgumentBonds();
        final double[] minObjectivesValueBonds = problem.getMinObjectivesValueBonds();
        final double[] maxObjectivesValueBonds = problem.getMaxObjectivesValueBonds();

        // initialize population and evaluatedValues
        population = new double[velpop][numberOfObjectives];
        evaluatedValues = new double[velpop][numberOfObjectives];
        fitnesses = new double[velpop];
        for (int i = 0; i < velpop; i++) {
            for (int j = 0; j < numberOfObjectives; j++) {
                population[i][j] = minArgumentBonds[j]
                        + rand.nextDouble() * (maxArgumentBonds[j] - minArgumentBonds[j]);
            }
            problem.evaluateSolution(population[i], evaluatedValues[i]);
        }

        double[][] nextPopulation = new double[velpop][numberOfObjectives];

        // do generations
        for (int i = 0; i < maxiter; i++) {
            List<Set<Integer>> fronts = NDS(); // non dominated sort
            double Fmin = velpop;
            for (Set<Integer> front : fronts) {
                double frontFitness = Fmin * EPSILON;
                for (int j : front) {
                    double ncFront = 0.0;
                    for (int k : front) {
                        double distance;
                        if (vrsta.equals("objective-space")) {
                            distance = distance(evaluatedValues[j], evaluatedValues[k],
                                    minObjectivesValueBonds, maxObjectivesValueBonds);
                        } else {
                            distance = distance(population[j], population[k],
                                    minArgumentBonds, maxArgumentBonds);
                        }
                        double sh = 0.0;
                        if (distance < SIGMA_SHARE) {
                            sh = 1 - Math.pow(distance / SIGMA_SHARE, ALPHA);
                        }
                        ncFront += sh;
                    }
                    fitnesses[j] = Fmin / ncFront;
                    if (fitnesses[j] < frontFitness) {
                        frontFitness = fitnesses[j];
                    }
                }
                Fmin = frontFitness;
            }

            for (int j = 0; j < velpop; j++) {
                int p1 = RoulleteWheelSelection.selection(rand, fitnesses);
                int p2 = RoulleteWheelSelection.selection(rand, fitnesses);
                double[] child = UniformCrossover.crossover(rand, population[p1], population[p2]);
                GaussianMutation.mutate(rand, child, minArgumentBonds, maxArgumentBonds);
                nextPopulation[j] = child;
                problem.evaluateSolution(child, evaluatedValues[j]);
            }

            double bestValue = fitnesses[0];
            int bestIndex = 0;
            for (int j = 1; j < velpop; j++) {
                if (fitnesses[j] > bestValue) bestIndex = j;
            }

            population = nextPopulation;
            System.out.println((i + 1) + ".iteracija: " + Arrays.toString(population[bestIndex]));
        }

        System.out.println("\nBroj fronti: " + NDS().size());
        writeToFiles();
    }

    private void writeToFiles() {
        try (PrintWriter pr1 = new PrintWriter(new File("izlaz-dec.txt"));
             PrintWriter pr2 = new PrintWriter(new File("izlaz-obj.txt"))) {
            StringBuilder sb1 = new StringBuilder();
            StringBuilder sb2 = new StringBuilder();
            int numberOfObjectives = problem.getNumberOfObjectives();
            for (int i = 1; i <= numberOfObjectives; i++) {
                sb1.append("X").append(i).append(",");
                sb2.append("f").append(i).append(",");
            }
            sb1.setLength(sb1.length() - 1);
            sb2.setLength(sb2.length() - 1);
            pr1.println(sb1.toString());
            pr2.println(sb2.toString());
            sb1.setLength(0);
            sb2.setLength(0);
            for (int i = 0; i < velpop; i++) {
                Arrays.stream(population[i]).forEach(r -> sb1.append(r).append(","));
                Arrays.stream(evaluatedValues[i]).forEach(r -> sb2.append(r).append(","));
                sb1.setLength(sb1.length() - 1);
                sb2.setLength(sb2.length() - 1);
                sb1.append("\n");
                sb2.append("\n");
            }
            pr1.println(sb1.toString());
            pr2.println(sb2.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private List<Set<Integer>> NDS() {
        List<Set<Integer>> dominations = new ArrayList<>();
        int[] dominatedBy = new int[velpop];
        List<Set<Integer>> fronts = new ArrayList<>();
        Set<Integer> optimalFront = new HashSet<>();
        fronts.add(optimalFront);

        for (int i = 0; i < velpop; i++) {
            Set<Integer> dominatesOn = new HashSet<>();
            for (int j = 0; j < velpop; j++) {
                if (i == j) continue;
                if (dominates(evaluatedValues[i], evaluatedValues[j])) {
                    dominatesOn.add(j);
                } else if (dominates(evaluatedValues[j], evaluatedValues[i])) {
                    dominatedBy[i]++;
                }
            }
            dominations.add(dominatesOn);
            if (dominatedBy[i] == 0) optimalFront.add(i);
        }

        while (!optimalFront.isEmpty()) {
            Set<Integer> newFront = new HashSet<>();
            for (int i : optimalFront) {
                for (int j : dominations.get(i)) {
                    dominatedBy[j]--;
                    if (dominatedBy[j] == 0) newFront.add(j);
                }
            }
            fronts.add(newFront);
            optimalFront = newFront;
        }
        return fronts;
    }

    private boolean dominates(double[] v1, double[] v2) {
        boolean oneComponentBetter = false;
        for (int i = 0; i < v1.length; i++) {
            if (v1[i] > v2[i]) return false;
            if (v1[i] < v2[i]) oneComponentBetter = true;
        }
        return oneComponentBetter;
    }

    private double distance(double[] v1, double[] v2, double[] mins, double[] maxs) {
        double distance = 0.0;
        for (int i = 0; i < v1.length; i++) {
            double v = (v1[i] - v2[i]) / (maxs[i] - mins[i]);
            distance += v * v;
        }
        return Math.sqrt(distance);
    }

}
