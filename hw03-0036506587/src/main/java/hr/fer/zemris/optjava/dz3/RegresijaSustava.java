package hr.fer.zemris.optjava.dz3;

import hr.fer.zemris.optjava.dz3.decoder.GreyBinaryDecoder;
import hr.fer.zemris.optjava.dz3.decoder.NaturalBinaryDecoder;
import hr.fer.zemris.optjava.dz3.decoder.PassThroughDecoder;
import hr.fer.zemris.optjava.dz3.function.FunctionImpl;
import hr.fer.zemris.optjava.dz3.function.IFunction;
import hr.fer.zemris.optjava.dz3.neighborhood.BitvectorNeighborhood;
import hr.fer.zemris.optjava.dz3.neighborhood.DoubleArrayNormNeighborhood;
import hr.fer.zemris.optjava.dz3.solution.BitvectorSolution;
import hr.fer.zemris.optjava.dz3.solution.DoubleArraySolution;
import hr.fer.zemris.optjava.dz3.temperature.GeometricTempSchedule;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class RegresijaSustava {

    private static final double ALPHA = 0.96;
    private static final int T_INITIAL = 10_000;
    private static final int INNER_LIMIT = 1_000;
    private static final int OUTER_LIMIT = 600;
    private static final int NUMBER_OF_VARS = 6;
    private static final Random RAND = new Random();

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.out.println("Program očekuje 2 argumenta!");
            return;
        }
        Path path = Paths.get(args[0]);
        String solutionType = args[1];
        IFunction function = parse(path);

        if (solutionType.equals("decimal")) {
            DoubleArraySolution startWith = new DoubleArraySolution(NUMBER_OF_VARS);
            double[] mins = new double[NUMBER_OF_VARS];
            double[] maxs = new double[NUMBER_OF_VARS];
            double[] deltas = new double[NUMBER_OF_VARS];
            Arrays.fill(mins, 1); // fill mins array
            Arrays.fill(maxs, 5); // fills maxs array
            Arrays.fill(deltas, 0.01); // fills deltas array
            startWith.randomize(RAND, mins, maxs);
            new SimulatedAnnealing<>(
                    new PassThroughDecoder(),
                    new DoubleArrayNormNeighborhood(deltas),
                    startWith,
                    function,
                    new GeometricTempSchedule(
                            ALPHA,
                            T_INITIAL,
                            INNER_LIMIT,
                            OUTER_LIMIT),
                    true
            ).run();
        } else if (solutionType.startsWith("binary")) {
            int bitsPerVar = Integer.parseInt(solutionType.substring(solutionType.indexOf(":") + 1));
            if (bitsPerVar < 5 || bitsPerVar > 30) {
                System.out.println("Pogrešan broj bitova po varijabli");
                return;
            }
            BitvectorSolution startWith = new BitvectorSolution(NUMBER_OF_VARS * bitsPerVar);
            startWith.randomize(RAND);
            new SimulatedAnnealing<>(
                    new GreyBinaryDecoder(-7, 7, bitsPerVar, NUMBER_OF_VARS),
                    new BitvectorNeighborhood(),
                    startWith,
                    function,
                    new GeometricTempSchedule(
                            ALPHA,
                            T_INITIAL,
                            INNER_LIMIT,
                            OUTER_LIMIT),
                    true
            ).run();
        } else {
            System.out.println("Pogrešna reprezentacija rješenja");
        }
    }

    private static IFunction parse(Path path) throws IOException {
        List<String> lines = Files.readAllLines(path).stream()
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
