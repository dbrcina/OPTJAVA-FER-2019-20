package hr.fer.zemris.optjava.dz7;

import hr.fer.zemris.optjava.dz7.algorithm.CLONALG;
import hr.fer.zemris.optjava.dz7.algorithm.PSOALG;
import hr.fer.zemris.optjava.dz7.dataset.IReadOnlyDataset;
import hr.fer.zemris.optjava.dz7.dataset.IrisDataset;
import hr.fer.zemris.optjava.dz7.function.ITransferFunction;
import hr.fer.zemris.optjava.dz7.function.SigmoidTransferFunction;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ANNTrainer {

    private static final int[] LAYERS = new int[]{4, 5, 3, 3};
    private static final ITransferFunction[] TRANSFER_FUNCTIONS = new ITransferFunction[]{
            new SigmoidTransferFunction(),
            new SigmoidTransferFunction(),
            new SigmoidTransferFunction()
    };

    public static void main(String[] args) {
        if (args.length != 5) {
            System.out.println("Program očekuje 5 argumenata");
            return;
        }

        // prepare arguments from command line
        Path file = Paths.get(args[0]);
        String algorithm = args[1];
        int popSize = Integer.parseInt(args[2]);
        double merr = Double.parseDouble(args[3]);
        int maxiter = Integer.parseInt(args[4]);

        // prepare dataset
        IReadOnlyDataset dataset = new IrisDataset();
        try {
            dataset.loadDataset(file);
        } catch (IOException ignored) {
        }

        // prepare neural network
        FFANN ffann = new FFANN(LAYERS, TRANSFER_FUNCTIONS, dataset);

        // run provided algorithm
        double[] weights = null;
        if (algorithm.equals("clonalg")) {
            weights = new CLONALG(ffann, popSize, merr, maxiter).run();
        } else if (algorithm.equals("pso-a")) {
            weights = new PSOALG(ffann, popSize, merr, maxiter, 0).run();
        } else if (algorithm.startsWith("pso-b")) {
            int localNeighbours = Integer.parseInt(algorithm.substring("pso-b".length() + 1));
            weights = new PSOALG(ffann, popSize, merr, maxiter, localNeighbours).run();
        } else {
            System.out.println("Pogrešan algoritam");
            return;
        }

        // print statistics
        ffann.statistics(weights);
    }

}
