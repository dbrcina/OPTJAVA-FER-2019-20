package hr.fer.zemris.optjava.dz7;

import hr.fer.zemris.optjava.dz7.dataset.IReadOnlyDataset;
import hr.fer.zemris.optjava.dz7.function.ITransferFunction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

public class FFANN {

    private int[] layers;
    private ITransferFunction[] functions;
    private IReadOnlyDataset dataset;
    private int weightsCount;

    public FFANN(int[] layers, ITransferFunction[] functions, IReadOnlyDataset dataset) {
        if (layers.length - 1 != functions.length) {
            throw new IllegalArgumentException("Broj prijenosnih funkcija mora biti jednak " +
                    "broju slojeva neuronske mreže - 1");
        }
        this.layers = layers;
        this.functions = functions;
        this.dataset = dataset;
        calculateWeightsCount();
    }

    private void calculateWeightsCount() {
        for (int i = 0; i < layers.length; i++) {
            if (i != 0) weightsCount += layers[i];
            if (i != layers.length - 1) weightsCount += layers[i] * layers[i + 1];
        }
    }

    public int getWeightsCount() {
        return weightsCount;
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
            int currentLayerCount = layers[i];
            double[] currentLayerOutputs = new double[currentLayerCount];
            for (int j = 0; j < currentLayerOutputs.length; j++) {
                // calculate net value
                // first previousLayerCount are outputs from previous layer * weights
                // last previousLayerCount + 1 is a bias weight
                int previosLayerCount = layers[i - 1];
                for (int k = 0; k < previosLayerCount + 1; k++) {
                    if (k == previosLayerCount) {
                        currentLayerOutputs[j] += weights[offset++];
                        continue;
                    }
                    currentLayerOutputs[j] += outputs[k] * weights[offset++];
                }
                // calculate transition function
                currentLayerOutputs[j] = functions[i - 1].valueAt(currentLayerOutputs[j]);
            }
            outputs = currentLayerOutputs;
        }

        return outputs;
    }

    public double errorFunction(double[] weights) {
        double error = 0.0;
        int numberOfSamples = dataset.numberOfSamples();
        for (int i = 0; i < numberOfSamples; i++) {
            Entry<double[], double[]> sample = dataset.inputsOutputsPair(i);
            double[] inputs = sample.getKey();
            double[] expectedOutputs = sample.getValue();
            double[] actualOutputs = calculateOutputs(inputs, weights);
            for (int j = 0; j < actualOutputs.length; j++) {
                error += Math.pow(expectedOutputs[j] - actualOutputs[j], 2);
            }
        }
        return error / numberOfSamples;
    }

    public void statistics(double[] optimalWeights) {
        int numberOfSamples = dataset.numberOfSamples();
        int goodCounter = 0;
        int badCounter = 0;
        List<String> badInputs = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numberOfSamples; i++) {
            Entry<double[], double[]> sample = dataset.inputsOutputsPair(i);
            double[] inputs = sample.getKey();
            double[] expectedOutputs = sample.getValue();
            double[] actualOutputs = calculateOutputs(inputs, optimalWeights);
            for (int j = 0; j < actualOutputs.length; j++) {
                actualOutputs[j] = actualOutputs[j] < 0.5 ? 0.0 : 1.0;
            }
            if (Arrays.equals(expectedOutputs, actualOutputs)) goodCounter++;
            else {
                badCounter++;
                sb.append("Ulazi:").append(Arrays.toString(inputs));
                sb.append("\n");
                sb.append("Očekivani izlazi:").append(Arrays.toString(expectedOutputs));
                sb.append("\n");
                sb.append("Dobiveni izlazi: ").append(Arrays.toString(actualOutputs));
                sb.append("\n");
                badInputs.add(sb.toString());
                sb.setLength(0);
            }

        }
        sb.append("\n");
        sb.append("Dobri izlazi:").append(goodCounter);
        sb.append("\n");
        sb.append("Loši izlazi: ").append(badCounter);
        sb.append("\n");
        System.out.println(sb.toString());

        System.out.println("Krivi rezultati za ulaze:");
        badInputs.forEach(System.out::println);
    }

}
