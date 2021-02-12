package hr.fer.zemris.optjava.dz8.neuralnetwork;

import hr.fer.zemris.optjava.dz8.dataset.IReadOnlyDataset;
import hr.fer.zemris.optjava.dz8.function.IFunction;

public class ElmanNN extends NeuralNetwork {

    private int[] layers;
    private IFunction[] transferFunctions;
    private double[] context;

    public ElmanNN(int[] layers, IFunction[] transferFunctions, IReadOnlyDataset dataset) {
        super(dataset);
        if (layers[0] != layers[layers.length - 1]) {
            throw new IllegalArgumentException("Ulazni i izlazni sloj moraju bit jednaki");
        }
        if (layers.length - 1 != transferFunctions.length) {
            throw new IllegalArgumentException("Broj prijenosnih funkcija mora biti jednak " +
                    "broju slojeva neuronske mreže - 1");
        }
        this.layers = layers;
        this.transferFunctions = transferFunctions;
        this.context = new double[layers[1]];
        calculateWeightsCount();
    }

    private void calculateWeightsCount() {
        for (int i = 0; i < layers.length; i++) {
            if (i != 0) weightsCount += layers[i];
            if (i != layers.length - 1) weightsCount += layers[i] * layers[i + 1];
        }
        weightsCount += context.length * layers[1];
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

        // initially outputs array is equal to inputs array + context
        double[] outputs = new double[inputs.length + context.length];
        System.arraycopy(inputs, 0, outputs, 0, inputs.length);
        System.arraycopy(context, 0, outputs, inputs.length, context.length);

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
                // context
                if (i == 1) {
                    for (int k = previosLayerNeurons; k < outputs.length; k++) {
                        currentLayerOutputs[j] += outputs[k] * weights[offset++];
                    }
                }
                // calculate transfer function
                currentLayerOutputs[j] = transferFunctions[i - 1].valueAt(currentLayerOutputs[j]);
            }
            if (i == 1) {
                context = currentLayerOutputs.clone();
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
        double[] inputs = new double[layers[0]];
        while (dataset.hasNext()) {
            if (counter < layers[0]) {
                inputs[counter++] = dataset.next();
                continue;
            }
            double expected = dataset.next();
            // array should contain only one value
            double actual = calculateOutputs(inputs, optimalWeights)[0];
            sb.append("Napravljena je statistika samo na prvih ")
                    .append(layers[0])
                    .append(" inputa.")
                    .append("\n");
            sb.append("Očekivano: ").append(expected).append("\n");
            sb.append("Dobiveno:  ").append(actual).append("\n");
            break;
        }
        return sb.toString();
    }

}
