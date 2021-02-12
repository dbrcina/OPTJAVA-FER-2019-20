package hr.fer.zemris.optjava.dz10.algorithm;

import hr.fer.zemris.optjava.dz10.crossover.UniformCrossover;
import hr.fer.zemris.optjava.dz10.mutation.GaussianMutation;
import hr.fer.zemris.optjava.dz10.problem.MOOPProblem;
import hr.fer.zemris.optjava.dz10.selection.CrowdedTournamentSelection;
import hr.fer.zemris.optjava.dz10.solution.Solution;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class NSGA2 {

    private final MOOPProblem problem;
    private final int velpop;
    private final int maxiter;
    private final int numberOfObjectives;
    private final double[] minArgumentBonds;
    private final double[] maxArgumentBonds;
    private final Random rand = new Random();

    private List<Solution> population;
    private List<List<Solution>> fronts;

    public NSGA2(MOOPProblem problem, int velpop, int maxiter) {
        this.problem = problem;
        this.velpop = velpop;
        this.maxiter = maxiter;
        numberOfObjectives = problem.getNumberOfObjectives();
        minArgumentBonds = problem.getMinArgumentBonds();
        maxArgumentBonds = problem.getMaxArgumentBonds();
    }

    public void run() {
        // initialize population
        initialize();

        // start generations
        for (int generation = 0; generation < maxiter; generation++) {
            // create children based on two best parents from population
            // and add them to existing population
            createChildren(bestParents());
            // non dominated sort
            nonDominatedSort();
            // generate new population
            generateNewPopulation();
            System.out.println("Generacija: " + (generation + 1));
        }
        nonDominatedSort();
        System.out.println("\nBroj fronti: " + fronts.size());
        writeToFiles();
    }

    private void initialize() {
        population = new ArrayList<>();
        for (int i = 0; i < velpop; i++) {
            double[] values = new double[numberOfObjectives];
            double[] objectives = new double[numberOfObjectives];
            for (int j = 0; j < numberOfObjectives; j++) {
                values[j] = minArgumentBonds[j]
                        + rand.nextDouble() * (maxArgumentBonds[j] - minArgumentBonds[j]);
            }
            problem.evaluateSolution(values, objectives);
            population.add(new Solution(values, objectives));
        }
    }

    private Solution[] bestParents() {
        return new Solution[]{
                CrowdedTournamentSelection.select(rand, population),
                CrowdedTournamentSelection.select(rand, population)
        };
    }

    private void createChildren(Solution[] parents) {
        List<Solution> children = new ArrayList<>();
        for (int i = 0; i < velpop; i++) {
            double[] values = UniformCrossover.crossover(rand, parents);
            GaussianMutation.mutate(rand, values, minArgumentBonds, maxArgumentBonds);
            double[] objectives = new double[numberOfObjectives];
            problem.evaluateSolution(values, objectives);
            children.add(new Solution(values, objectives));
        }
        population.addAll(children);
    }

    private void nonDominatedSort() {
        fronts = new ArrayList<>();
        Map<Solution, List<Solution>> dominationsOn = new HashMap<>();
        Map<Solution, Integer> dominatedBy = new HashMap<>();
        List<Solution> optimalFront = new ArrayList<>();
        fronts.add(optimalFront);

        for (int i = 0; i < population.size(); i++) {
            Solution iSolution = population.get(i);
            List<Solution> dominatesOn = new ArrayList<>();
            int ni = 0;
            for (int j = 0; j < population.size(); j++) {
                if (i == j) continue;
                Solution jSolution = population.get(j);
                if (dominates(iSolution.getObjectives(), jSolution.getObjectives())) {
                    dominatesOn.add(jSolution);
                } else if (dominates(jSolution.getObjectives(), iSolution.getObjectives())) {
                    ni++;
                }
            }
            dominationsOn.put(iSolution, dominatesOn);
            dominatedBy.put(iSolution, ni);
            if (ni == 0) optimalFront.add(iSolution);
        }

        while (!optimalFront.isEmpty()) {
            List<Solution> newFront = new ArrayList<>();
            for (Solution i : optimalFront) {
                for (Solution j : dominationsOn.get(i)) {
                    dominatedBy.merge(j, 1, (oldVal, one) -> oldVal - one);
                    if (dominatedBy.get(j) == 0) newFront.add(j);
                }
            }
            fronts.add(newFront);
            optimalFront = newFront;
        }

        // update front numbers
        for (int i = 0; i < fronts.size(); i++) {
            final int frontRank = i;
            fronts.get(i).forEach(solution -> solution.setFrontRank(frontRank));
        }
    }

    private boolean dominates(double[] v1, double[] v2) {
        boolean oneComponentBetter = false;
        for (int i = 0; i < v1.length; i++) {
            if (v1[i] > v2[i]) return false;
            if (v1[i] < v2[i]) oneComponentBetter = true;
        }
        return oneComponentBetter;
    }

    private void generateNewPopulation() {
        List<Solution> newPopulation = new ArrayList<>();
        for (List<Solution> front : fronts) {
            if (front.isEmpty()) break;
            // calculate crowding distances for provided front
            calculateCrowdingDistances(front);
            if (newPopulation.size() + front.size() <= velpop) {
                newPopulation.addAll(front);
                continue;
            }
            front.sort(Solution::compareTo);
            int index = front.size() - 1;
            while (newPopulation.size() < velpop) newPopulation.add(front.get(index--));
        }
        population = new ArrayList<>(newPopulation);
    }

    private void calculateCrowdingDistances(List<Solution> front) {
        // reset crowding distances
        front.forEach(solution -> solution.setCrowdingDistance(0));
        for (int i = 0; i < numberOfObjectives; i++) {
            final int index = i;
            front.sort(Comparator.comparingDouble(s -> s.getObjectives()[index]));
            Solution min = front.get(0);
            Solution max = front.get(front.size() - 1);
            double range = max.getObjectives()[i] - min.getObjectives()[i];
            min.setCrowdingDistance(Double.MIN_VALUE);
            max.setCrowdingDistance(Double.MAX_VALUE);

            // skip if there aren't more than 3 solutions in a front
            if (front.size() < 3) continue;

            for (int j = 1; j < front.size() - 1; j++) {
                Solution temp = front.get(j);
                double before = front.get(j - 1).getObjectives()[i];
                double after = front.get(j + 1).getObjectives()[i];
                double crwDistance = temp.getCrowdingDistance();
                temp.setCrowdingDistance(crwDistance + crowdingDistance(before, after, range));
            }
        }
    }

    private double crowdingDistance(double before, double after, double range) {
        return (after - before) / range;
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
                Arrays.stream(population.get(i).getValues()).forEach(r -> sb1.append(r).append(","));
                Arrays.stream(population.get(i).getObjectives()).forEach(r -> sb2.append(r).append(","));
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

}
