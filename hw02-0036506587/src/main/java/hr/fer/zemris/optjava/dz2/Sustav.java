package hr.fer.zemris.optjava.dz2;

import hr.fer.zemris.optjava.dz2.functions.Function3;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import static hr.fer.zemris.optjava.dz2.NumOptAlgorithms.minGradientDescent;
import static hr.fer.zemris.optjava.dz2.NumOptAlgorithms.minNewton;

public class Sustav {

    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            System.out.println("Program očekuje 3 argumenta!");
            return;
        }

        String algorithm = args[0];
        int iterations = Integer.parseInt(args[1]);
        Path file = Paths.get("src/main/resources/" + args[2]);

        List<String> lines = Files.readAllLines(file).stream()
                .filter(line -> !line.startsWith("#"))
                .map(line -> line.replaceAll("[\\[\\]]", ""))
                .collect(Collectors.toList());

        RealMatrix coefficients = new Array2DRowRealMatrix(10, 10);
        RealMatrix b = new Array2DRowRealMatrix(10, 1);
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] parts = line.split(",\\s+");
            double[] values = new double[parts.length - 1];
            for (int j = 0; j < values.length; j++) {
                values[j] = Double.parseDouble(parts[j]);
            }
            coefficients.setRow(i, values);
            b.setEntry(i, 0, Double.parseDouble(parts[parts.length - 1]));
        }

        Function3 function = new Function3(coefficients, b);
        List<RealMatrix> results = null;
        switch (algorithm) {
            case "grad":
                results = minGradientDescent(function, iterations, null);
                break;
            case "newton":
                results = minNewton(function, iterations, null);
                break;
            default:
                System.out.println("Nepoznat algoritam je zadan!");
                System.exit(-1);
        }

        RealMatrix result = results.get(results.size() - 1);
        System.out.print("Results:\n[");
        StringJoiner sj = new StringJoiner(",");
        for (double xi : result.getRow(0)) {
            sj.add(Double.toString(xi));
        }
        System.out.print(sj);
        System.out.println("]");
        System.out.println("Error: " + function.getValue(result) * 100 + "%");
    }
}
