package hr.fer.zemris.optjava.dz8;

import hr.fer.zemris.optjava.dz8.algorithm.diffevolution.DifferentialEvolution;
import hr.fer.zemris.optjava.dz8.dataset.IReadOnlyDataset;
import hr.fer.zemris.optjava.dz8.dataset.LaserDataset;
import hr.fer.zemris.optjava.dz8.function.IFunction;
import hr.fer.zemris.optjava.dz8.function.TangensHyperbolicusTransferFunction;
import hr.fer.zemris.optjava.dz8.neuralnetwork.ElmanNN;
import hr.fer.zemris.optjava.dz8.neuralnetwork.NeuralNetwork;
import hr.fer.zemris.optjava.dz8.neuralnetwork.TDNN;

import java.io.IOException;
import java.util.Arrays;

public class ANNTrainer {

    private static final double A = -1.0;
    private static final double B = 1.0;

    private static final int NUMBER_OF_SAMPLES_TO_BE_TESTED = 600;

    private static final String STRATEGY = "best/1/bin";

    private static final double F = 0.3;
    private static final double CR = 0.1;

    public static void main(String[] args) {
        if (args.length != 5) {
            System.out.println("Program očekuje 5 argumenata");
            return;
        }

        // neural network layers
        int[] layers = initNeuralNetworkLayers(args[1].toLowerCase()); // net-arh
        // neural network transfer functions
        IFunction[] transferFunctions = initNeuralNetworkTransferFunctions(layers.length - 1);

        // dataset
        IReadOnlyDataset dataset = initDataset(args[0], layers[0]);

        // neural network
        NeuralNetwork network = initNeuralNetwork(
                args[1].toLowerCase(), // net-arh
                layers,
                transferFunctions,
                dataset
        );
        int popSize = Integer.parseInt(args[2]);
        double merr = Double.parseDouble(args[3]);
        int maxiter = Integer.parseInt(args[4]);

        DifferentialEvolution alg = new DifferentialEvolution(
                network,
                popSize,
                merr,
                maxiter,
                A,
                B,
                STRATEGY,
                F,
                CR
        );
        double[] optimalWeights = alg.run();
        System.out.println(network.statistics(optimalWeights));
    }

    private static NeuralNetwork initNeuralNetwork(
            String net,
            int[] layers,
            IFunction[] transferFunctions,
            IReadOnlyDataset dataset) {
        if (net.startsWith("tdnn")) return new TDNN(layers, transferFunctions, dataset);
        return new ElmanNN(layers, transferFunctions, dataset);
    }

    private static IFunction[] initNeuralNetworkTransferFunctions(int size) {
        IFunction[] transferFunctions = new IFunction[size];
        Arrays.fill(transferFunctions, new TangensHyperbolicusTransferFunction());
        return transferFunctions;
    }

    private static int[] initNeuralNetworkLayers(String net) {
        int[] layers;
        if (net.startsWith("tdnn-") || net.startsWith("elman-")) {
            try {
                layers = parseLayers(net.substring(net.indexOf("-") + 1));
            } catch (Exception e) {
                throw new IllegalArgumentException("Arhitektura mreže je pogrešno zadana");
            }
        } else {
            throw new IllegalArgumentException("Ime neuronske mreže je pogrešno zadano");
        }
        return layers;
    }

    private static int[] parseLayers(String arh) {
        return Arrays.stream(arh.split("x"))
                .mapToInt(Integer::parseInt)
                .toArray();
    }

    private static IReadOnlyDataset initDataset(
            String file,
            int windowsLength) {
        IReadOnlyDataset dataset = new LaserDataset();
        try {
            dataset.loadDataset(file, windowsLength, NUMBER_OF_SAMPLES_TO_BE_TESTED);
        } catch (IOException e) {
            e.printStackTrace();
        }
        dataset.transformValues(A, B);
        return dataset;
    }

}
