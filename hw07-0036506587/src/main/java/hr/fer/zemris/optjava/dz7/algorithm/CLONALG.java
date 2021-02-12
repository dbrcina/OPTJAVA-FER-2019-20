package hr.fer.zemris.optjava.dz7.algorithm;

import hr.fer.zemris.optjava.dz7.FFANN;

import java.util.*;

public class CLONALG {

    private final int popSize;
    private final double merr;
    private final int maxiter;
    private final FFANN paramAg;
    private final int paramN;
    private final int paramD = 5;
    private final int paramBeta = 2;
    private final double paramRho = 0.25;
    private final double lowerBond = -1.0;
    private final double upperBond = 1.0;
    private final Random rand = new Random();

    private double[][] population;

    public CLONALG(FFANN paramAg, int popSize, double merr, int maxiter) {
        this.popSize = popSize;
        this.merr = merr;
        this.maxiter = maxiter;
        this.paramAg = paramAg;
        this.paramN = popSize;
        initialize();
    }

    private void initialize() {
        population = new double[popSize][paramAg.getWeightsCount()];
        for (int i = 0; i < popSize; i++) {
            population[i] = randomValues();
        }
    }

    private double[] randomValues() {
        return rand.doubles(paramAg.getWeightsCount(), lowerBond, upperBond + 1).toArray();
    }

    public double[] run() {
        for (int i = 0; i < maxiter; i++) {
            sortPopulationByError();
            chooseN();
            cloning();
            hypermutation();
            sortPopulationByError();
            chooseN();
            birthAndReplace();
            double error = paramAg.errorFunction(population[0]);
            System.out.println((i + 1) + ". iteracija: " + error);
            if (error <= merr) break;
        }
        return population[0];
    }

    private void sortPopulationByError() {
        Arrays.sort(population, Comparator.comparingDouble(paramAg::errorFunction));
    }

    private void chooseN() {
        population = Arrays.stream(population)
                .limit(paramN)
                .toArray(double[][]::new);
    }

    private void cloning() {
        List<double[]> clonedUnits = new ArrayList<>();
        for (int i = 0; i < population.length; i++) {
            int copies = (int) (paramBeta * paramN / ((double) i + 1));
            for (int j = 0; j < copies; j++) {
                clonedUnits.add(population[i].clone());
            }
        }
        population = clonedUnits.toArray(double[][]::new);
    }

    private void hypermutation() {
        double avgError = 0.0;
        for (double[] unit : population) {
            avgError += paramAg.errorFunction(unit);
        }
        avgError /= population.length;
        for (int i = 1; i < population.length; i++) {
            double[] unit = population[i];
            double probability =
                    Math.exp(-paramRho * paramAg.errorFunction(unit) / avgError);
            for (int j = 0; j < unit.length; j++) {
                if (rand.nextDouble() < probability) {
                    unit[j] += rand.nextGaussian();
                }
            }
        }
    }

    private void birthAndReplace() {
        int offset = population.length - paramD;
        for (int i = 0; i < paramD; i++) {
            population[offset + i] = randomValues();
        }
    }

}
