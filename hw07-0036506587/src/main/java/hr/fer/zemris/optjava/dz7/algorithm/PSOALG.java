package hr.fer.zemris.optjava.dz7.algorithm;

import hr.fer.zemris.optjava.dz7.FFANN;

import java.util.Arrays;
import java.util.Random;

public class PSOALG {

    private final FFANN ffann;
    private final int popSize;
    private final double merr;
    private final int maxiter;
    private final int localNeighbours;
    private final int dimension;
    private final double xmin = -1.0;
    private final double xmax = 1.0;
    private final double vmin = -5.0;
    private final double vmax = 5.0;
    private final double c1 = 2.0;
    private final double c2 = 2.0;
    private final double inertionMin = 0.4;
    private final double inertionMax = 0.9;
    private final int inertionTreshold = 50;
    private final Random rand = new Random();

    private double[][] x;
    private double[][] v;
    private double[] f;

    private double[][] pbest;
    private double[] pbestF;

    private double[] best;
    private double bestF;

    public PSOALG(FFANN ffann, int popSize, double merr, int maxiter, int localNeighbours) {
        this.ffann = ffann;
        this.popSize = popSize;
        this.merr = merr;
        this.maxiter = maxiter;
        this.localNeighbours = localNeighbours;
        this.dimension = ffann.getWeightsCount();
        this.x = new double[popSize][dimension];
        this.v = new double[popSize][dimension];
        this.f = new double[popSize];
        this.pbest = new double[popSize][dimension];
        this.pbestF = new double[popSize];
        this.best = new double[dimension];
        initialize();
    }

    private void initialize() {
        for (int i = 0; i < popSize; i++) {
            x[i] = randomNumbers(xmin, xmax + 1);
            v[i] = randomNumbers(vmin, vmax + 1);
        }
        Arrays.fill(pbestF, Double.MAX_VALUE);
        bestF = Double.MAX_VALUE;
    }

    private double[] randomNumbers(double lowerBond, double upperBond) {
        return rand.doubles(dimension, lowerBond, upperBond).toArray();
    }

    public double[] run() {
        double w;
        for (int i = 0; i < maxiter; i++) {
            evaluate();
            updatePersonalBests();
            updateNeighbourhood();
            w = updateInertion(i);
            updateVAndX(w);
            System.out.println((i + 1) + ". iteracija: " + bestF);
            if (bestF <= merr) break;
        }
        return best;
    }

    public void evaluate() {
        for (int i = 0; i < popSize; i++) {
            f[i] = ffann.errorFunction(x[i]);
        }
    }

    private void updatePersonalBests() {
        for (int i = 0; i < popSize; i++) {
            if (f[i] < pbestF[i]) {
                pbestF[i] = f[i];
                pbest[i] = x[i].clone();
            }
        }
    }

    private void updateNeighbourhood() {
        if (localNeighbours == 0) updateGlobalBest();
        else updateLocalBest();
    }

    private void updateGlobalBest() {
        for (int i = 0; i < popSize; i++) {
            if (f[i] < bestF) {
                bestF = f[i];
                best = x[i].clone();
            }
        }
    }

    private void updateLocalBest() {
        for (int i = 0; i < popSize; i++) {
            int startFrom = i - localNeighbours / 2;
            int endAt = i + localNeighbours / 2;
            if (startFrom < 0) startFrom = 0;
            if (endAt >= popSize) endAt = popSize - 1;
            for (int neighbour = startFrom; neighbour <= endAt; neighbour++) {
                if (f[neighbour] < bestF) {
                    bestF = f[neighbour];
                    best = x[neighbour].clone();
                }
            }
        }
    }

    private double updateInertion(int i) {
        if (i > inertionTreshold) return inertionMin;
        return inertionMax + (inertionMin - inertionMax) * (i - 1) / inertionTreshold;
    }

    private void updateVAndX(double inertion) {
        for (int i = 0; i < popSize; i++) {
            for (int d = 0; d < dimension; d++) {
                v[i][d] = inertion * v[i][d] + c1 * rand.nextDouble() * (pbest[i][d] - x[i][d])
                        + c2 * rand.nextDouble() * (best[d] - x[i][d]);
                v[i][d] = checkBounds(v[i][d]);
                x[i][d] += v[i][d];
            }
        }
    }

    private double checkBounds(double v) {
        if (v < vmin) return vmin;
        if (v > vmax) return vmax;
        return v;
    }

}
