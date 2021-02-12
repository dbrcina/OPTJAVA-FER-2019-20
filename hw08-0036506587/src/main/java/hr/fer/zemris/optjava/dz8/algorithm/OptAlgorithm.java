package hr.fer.zemris.optjava.dz8.algorithm;

import hr.fer.zemris.optjava.dz8.neuralnetwork.NeuralNetwork;

import java.util.Random;

/**
 * Simple model of optimization algorithm.
 */
public abstract class OptAlgorithm {

    private final NeuralNetwork network;
    private final int popSize;
    private final double merr;
    private final int maxiter;
    private final double A;
    private final double B;
    private final Random rand;

    /**
     * Constructor.
     *
     * @param network an instance of {@link NeuralNetwork}.
     * @param popSize population size.
     * @param merr    minimum error.
     * @param maxiter maximum number of iterations.
     * @param A       lower bound for population initialization.
     * @param B       upper bound for populaton initialization.
     */
    public OptAlgorithm(
            NeuralNetwork network,
            int popSize,
            double merr,
            int maxiter,
            double A,
            double B) {
        this.network = network;
        this.popSize = popSize;
        this.merr = merr;
        this.maxiter = maxiter;
        this.A = A;
        this.B = B;
        this.rand = new Random();
    }

    public NeuralNetwork getNetwork() {
        return network;
    }

    public int getPopSize() {
        return popSize;
    }

    public double getMerr() {
        return merr;
    }

    public int getMaxiter() {
        return maxiter;
    }

    public double getA() {
        return A;
    }

    public double getB() {
        return B;
    }

    public Random getRand() {
        return rand;
    }

    /**
     * Runs the algorithm.
     *
     * @return an array of doubles as a solution.
     */
    public abstract double[] run();

}
