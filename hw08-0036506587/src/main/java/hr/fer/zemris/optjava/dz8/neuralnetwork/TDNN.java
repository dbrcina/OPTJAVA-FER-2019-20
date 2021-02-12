package hr.fer.zemris.optjava.dz8.neuralnetwork;

import hr.fer.zemris.optjava.dz8.dataset.IReadOnlyDataset;
import hr.fer.zemris.optjava.dz8.function.IFunction;

import java.util.Arrays;

/**
 * An implementation of <i><b>Time Delay Neural Network</b></i>.
 */
public class TDNN extends NeuralNetwork {

    private int[] layers;
    private IFunction[] transferFunctions;
    private int windowsLength;

    public TDNN(int[] layers, IFunction[] transferFunctions, IReadOnlyDataset dataset) {
        super(dataset);
        if (layers.length - 1 != transferFunctions.length) {
            throw new IllegalArgumentException("Broj prijenosnih funkcija mora biti jednak " +
                    "broju slojeva neuronske mreže - 1");
        }
        this.layers = layers;
        this.transferFunctions = transferFunctions;
        this.windowsLength = layers[0];
        calculateWeightsCount();
    }

    public int getWindowsLength() {
        return windowsLength;
    }

    private void calculateWeightsCount() {
        for (int i = 0; i < layers.length; i++) {
            if (i != 0) weightsCount += layers[i];
            if (i != layers.length - 1) weightsCount += layers[i] * layers[i + 1];
        }
    }

    public double[] calculateOutputs(double[] inputs, double[] weights) {
        int inputsLayerCount = layers[0];
        if (inputsLayerCount != inputs.length) {
            throw new IllegalArgumentException("Broj elemenata ulaznog polja se razlikuje od" +
                    " broja elemenata ulaznog sloja neuronske mreže");
        }
        if (weightsCount != weights.length) {
            throw new IllegalArgumentException("Broj elemenata predanog weights polja se " +
                    "razlikuje od broja elemenata weights polja neuronske mreže");
        }

        // initially outputs array is equal to inputs array
        double[] outputs = Arrays.copyOf(inputs, inputsLayerCount);

        int offset = 0;
        // skip first layer, already checked with the inputs array
        for (int i = 1; i < layers.length; i++) {
            int currentLayerNeurons = layers[i];
            double[] currentLayerOutputs = new double[currentLayerNeurons];
            for (int j = 0; j < currentLayerOutputs.length; j++) {
                // calculate net value
                // first previousLayerNeurons are outputs from previous layer * weights
                // last previousLayerNeurons + 1 is a bias weight
                int previosLayerNeurons = layers[i - 1];
                for (int k = 0; k < previosLayerNeurons + 1; k++) {
                    if (k == previosLayerNeurons) {
                        // bias
                        currentLayerOutputs[j] += weights[offset++];
                        continue;
                    }
                    currentLayerOutputs[j] += outputs[k] * weights[offset++];
                }
                // calculate transfer function
                currentLayerOutputs[j] = transferFunctions[i - 1].valueAt(currentLayerOutputs[j]);
            }
            outputs = currentLayerOutputs;
        }

        return outputs;
    }

    @Override
    public String statistics(double[] optimalWeights) {
        StringBuilder sb = new StringBuilder();
        IReadOnlyDataset dataset = getDataset();
        int counter = 0;
        double[] inputs = new double[windowsLength];
        while (dataset.hasNext()) {
            if (counter < windowsLength) {
                inputs[counter++] = dataset.next();
                continue;
            }
            double expected = dataset.next();
            // array should contain only one value
            double actual = calculateOutputs(inputs, optimalWeights)[0];
            sb.append("Napravljena je statistika samo na prvih ")
                    .append(windowsLength)
                    .append(" inputa.")
                    .append("\n");
            sb.append("Očekivano: ").append(expected).append("\n");
            sb.append("Dobiveno:  ").append(actual).append("\n");
            break;
        }
        return sb.toString();
    }

}
