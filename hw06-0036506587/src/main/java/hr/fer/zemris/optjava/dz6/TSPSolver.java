package hr.fer.zemris.optjava.dz6;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class TSPSolver {

    private static final double ALPHA = 1;
    private static final double BETA = 5;
    private static final double RHO = 0.02;
    private static final double P = 0.9;

    public static void main(String[] args) throws IOException {
        if (args.length != 4) {
            System.out.println("Program očekuje 4 argumenta");
            return;
        }

        Path tspFile = Paths.get(args[0]);
        int numberOfCandidates = Integer.parseInt(args[1]);
        int maxAnts = Integer.parseInt(args[2]);
        int maxIterations = Integer.parseInt(args[3]);
        double[][] points2D = points2D(tspFile);
        new MMAS(
                points2D,
                numberOfCandidates,
                maxAnts,
                maxIterations,
                ALPHA,
                BETA,
                calculateA(points2D.length),
                RHO
        ).run();
    }

    private static double calculateA(double n) {
        return (n - 1) / (-1 + Math.pow(P, -1 / n));
    }

    private static double[][] points2D(Path tspFile) throws IOException {
        List<String> lines = Files.readAllLines(tspFile)
                .stream()
                .filter(line -> Character.isDigit(line.charAt(0)))
                .collect(Collectors.toList());
        int size = lines.size();
        double[][] points = new double[size][2];
        for (int i = 0; i < size; i++) {
            String[] parts = lines.get(i).split("\\s+");
            points[i][0] = Double.parseDouble(parts[1]);
            points[i][1] = Double.parseDouble(parts[2]);
        }
        return points;
    }

}
