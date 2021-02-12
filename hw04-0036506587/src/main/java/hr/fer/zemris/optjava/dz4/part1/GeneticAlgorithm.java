package hr.fer.zemris.optjava.dz4.part1;

import hr.fer.zemris.optjava.dz4.part1.function.FunctionImpl;
import hr.fer.zemris.optjava.dz4.part1.function.IFunction;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

import static hr.fer.zemris.optjava.dz4.part1.Population.Unit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Generational genetic algorithm (<b>ELITIST</b>)
 */
public class GeneticAlgorithm {

    private static final Path PATH = Paths.get("src/main/resources/zad-prijenosna.txt");
    private static final int NUMBER_OF_VARIABLES = 6;
    private static final double ALPHA = 0.1;
    private static final Random RAND = new Random();
    private static final int FIRST = 10;
    private static final int NUMBER_OF_PARENTS = 2;
    private static final int NUMBER_OF_CHILDREN = 2;

    public static IFunction function;

    public static void main(String[] args) throws IOException {
        if (args.length != 5) {
            System.out.println("Program očekuje 5 argumenata");
            return;
        }

        // arguments from command line
        int velPop = Integer.parseInt(args[0]);
        double delta = Double.parseDouble(args[1]);
        int maxGenerations = Integer.parseInt(args[2]);
        String selection = args[3];
        double sigma = Double.parseDouble(args[4]);

        // function
        function = parse();

        // do algorithm
        Unit solution = doAlgorithm(velPop, delta, maxGenerations, selection, sigma);

        // print result
        System.out.println("Najbolje rješenje:");
        System.out.println(solution);
    }

    private static Unit doAlgorithm(
            int velPop,
            double delta,
            int maxGenerations,
            String selection,
            double sigma) {
        // initial population
        Population population = new Population(NUMBER_OF_VARIABLES, velPop);
        double[] min = new double[NUMBER_OF_VARIABLES];
        double[] max = new double[NUMBER_OF_VARIABLES];
        Arrays.fill(min, -5);
        Arrays.fill(max, 5);
        population.randomize(RAND, min, max);

        for (int i = 0; i < maxGenerations; i++) {
            if (evaluate(population, delta)) return population.getBestUnit();
            // new population with zero units
            Population newPopulation = new Population(NUMBER_OF_VARIABLES);
            // add two best units
            newPopulation.addUnits(population.getUnits().stream()
                    .limit(2)
                    .toArray(Unit[]::new)
            );
            while (newPopulation.getUnits().size() < velPop) {
                Unit[] parents = selection(selection, population);
                Unit[] children = BLXaCrossover(parents);
                for (Unit child : children) {
                    child.mutate(RAND, sigma);
                }
                newPopulation.addUnits(children);
            }
            population = newPopulation;
            System.out.println((i + 1) + ".ta generacija: " + population.getBestUnit());
        }

        return population.getBestUnit();
    }

    private static boolean evaluate(Population population, double delta) {
        // set is sorted in ascending order based on fitness,
        // so the first element should be the best
        Unit bestUnit = population.getBestUnit();
        return Math.abs(bestUnit.getFitness()) <= delta;
    }

    private static Unit[] selection(String selection, Population population) {
        if (selection.equals("rouletteWheel")) {
            return rouletteWheelSelection(population);
        }
        if (selection.startsWith("tournament")) {
            int k = Integer.parseInt(selection.substring(selection.indexOf(":") + 1));
            if (k < 2) {
                throw new IllegalArgumentException("k mora biti >= 2");
            }
            return tournamentSelection(population, k);
        }
        throw new IllegalArgumentException("Pogrešan selection");
    }

    private static Unit[] tournamentSelection(Population population, int k) {
        Unit[] parents = new Unit[NUMBER_OF_PARENTS];
        List<Unit> units = new ArrayList<>(population.getUnits());
        for (int i = 0; i < parents.length; i++) {
            Set<Unit> setOfUnits = new TreeSet<>();
            for (int j = 0; j < k; j++) {
                setOfUnits.add(units.get(RAND.nextInt(units.size())));
            }
            parents[i] = setOfUnits.iterator().next();
        }
        return parents;
    }

    private static Unit[] rouletteWheelSelection(Population population) {
        Unit[] parents = new Unit[NUMBER_OF_PARENTS];
        Unit[] units = population.first(FIRST);
        int n = units.length;
        double worstFitness = units[n - 1].getFitness();
        double sum = Arrays.stream(units)
                .mapToDouble(Unit::getFitness)
                .sum();
        double prevProb = 0;
        double[] percentages = new double[n];
        for (int i = 0; i < n; i++) {
            Unit u = units[i];
            percentages[i] = prevProb + u.getFitness() / sum;
            prevProb = percentages[i];
        }

        for (int i = 0; i < parents.length; i++) {
            double random = RAND.nextDouble();
            for (int j = 0; j < percentages.length; j++) {
                if (random < percentages[j]) {
                    parents[i] = units[j];
                    break;
                }

            }
        }

        return parents;
    }

    private static Unit[] BLXaCrossover(Unit[] parents) {
        Unit[] children = new Unit[NUMBER_OF_CHILDREN];
        for (int j = 0; j < children.length; j++) {
            Unit child = new Unit();
            double[] values = new double[NUMBER_OF_VARIABLES];
            for (int i = 0; i < NUMBER_OF_VARIABLES; i++) {
                double ci1 = parents[0].getValues()[i];
                double ci2 = parents[1].getValues()[i];
                double ciMin = Math.min(ci1, ci2);
                double ciMax = Math.max(ci1, ci2);
                double Ii = ciMax - ciMin;
                double lowerBound = ciMin - Ii * ALPHA;
                double upperBound = ciMax + Ii * ALPHA;
                values[i] = lowerBound + RAND.nextDouble() * (upperBound - lowerBound);
            }
            child.setValues(values);
            children[j] = child;
        }
        return children;
    }


    private static IFunction parse() throws IOException {
        List<String> lines = Files.readAllLines(PATH).stream()
                .filter(line -> !line.startsWith("#"))
                .map(line -> line.replaceAll("[\\[\\]]", ""))
                .collect(Collectors.toList());

        int rows = lines.size();
        int columns = 5;
        RealMatrix coefficients = new Array2DRowRealMatrix(rows, columns);
        RealMatrix y = new Array2DRowRealMatrix(rows, 1);
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] parts = line.split(",\\s+");
            double[] values = new double[parts.length - 1];
            for (int j = 0; j < values.length; j++) {
                values[j] = Double.parseDouble(parts[j]);
            }
            coefficients.setRow(i, values);
            y.setEntry(i, 0, Double.parseDouble(parts[parts.length - 1]));
        }
        return new FunctionImpl(coefficients, y);
    }
}
