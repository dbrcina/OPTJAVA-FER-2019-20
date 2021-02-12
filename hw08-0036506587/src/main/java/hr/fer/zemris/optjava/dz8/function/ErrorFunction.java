package hr.fer.zemris.optjava.dz8.function;

import hr.fer.zemris.optjava.dz8.dataset.IReadOnlyDataset;
import hr.fer.zemris.optjava.dz8.neuralnetwork.NeuralNetwork;
import hr.fer.zemris.optjava.dz8.neuralnetwork.TDNN;

/**
 * An implementation of <i>Mean squared</i> error function.
 */
public class ErrorFunction {

    /**
     * Calculates error for provided neural network and its weights.
     *
     * @param network neural network.
     * @param weights an array of weights.
     * @return calculated error as a result.
     */
    public static double calculateError(NeuralNetwork network, double[] weights) {
        double error = 0.0;
        IReadOnlyDataset dataset = network.getDataset();
        int windowsLength;
        if (network instanceof TDNN) {
            windowsLength = ((TDNN) network).getWindowsLength();
        } else {
            windowsLength = 1;
        }
        double[] inputs = new double[windowsLength];
        int counter = 0;
        while (dataset.hasNext()) {
            // read windowsLength number of samples
            if (counter < windowsLength) {
                inputs[counter++] = dataset.next();
                continue;
            }
            // windowsLength + 1 is expected value
            double expected = dataset.next();
            // array should contain only one element
            double[] actual = network.calculateOutputs(inputs, weights);
            double result = expected - actual[0];
            error += result * result;
            counter = 0;
        }
        dataset.resetCounter();
        return error / dataset.numberOfSamplesToBeTested();
    }

}
