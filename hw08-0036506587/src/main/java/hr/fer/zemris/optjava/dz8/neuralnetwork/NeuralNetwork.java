package hr.fer.zemris.optjava.dz8.neuralnetwork;

import hr.fer.zemris.optjava.dz8.dataset.IReadOnlyDataset;

/**
 * Simple model of <i>Neural network</i>.
 */
public abstract class NeuralNetwork {

    private IReadOnlyDataset dataset;
    protected int weightsCount;

    public NeuralNetwork(IReadOnlyDataset dataset) {
        this.dataset = dataset;
    }

    /**
     * @return dataset used for training.
     */
    public IReadOnlyDataset getDataset() {
        return dataset;
    }

    /**
     * @return calculated weights.
     */
    public int getWeightsCount() {
        return weightsCount;
    }

    /**
     * Calculates outputs based on <i>inputs</i> and <i>weights</i>.
     *
     * @param inputs  an array of inputs.
     * @param weights an array of weights.
     * @return calculated outputs.
     */
    public abstract double[] calculateOutputs(double[] inputs, double[] weights);

    /**
     * This method is used for statistics based on <i>optimalWeights</i>.
     *
     * @param optimalWeights optimal weights.
     * @return string representation of statistics.
     */
    public abstract String statistics(double[] optimalWeights);

}
