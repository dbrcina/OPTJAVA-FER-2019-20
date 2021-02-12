package hr.fer.zemris.optjava.dz8.algorithm.diffevolution.selection;

import hr.fer.zemris.optjava.dz8.algorithm.diffevolution.Util;
import hr.fer.zemris.optjava.dz8.neuralnetwork.NeuralNetwork;

/**
 * An implementation of {@link Selection} interface.
 */
public class DifferentialSelection implements Selection<double[]> {

    private final NeuralNetwork network;

    public DifferentialSelection(NeuralNetwork network) {
        this.network = network;
    }

    @Override
    public double[] select(double[] t1, double[] t2) {
        if (Util.evaluate(network, t1) <= Util.evaluate(network, t2)) {
            return t1;
        }
        return t2;
    }
}
