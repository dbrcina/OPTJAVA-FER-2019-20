package hr.fer.zemris.optjava.dz8.dataset;

import java.io.IOException;
import java.util.Iterator;

import hr.fer.zemris.optjava.dz8.neuralnetwork.NeuralNetwork;

public interface IReadOnlyDataset extends Iterator<Double> {

    /**
     * Loads data from provided <i>file</i> and prepares it for {@link NeuralNetwork}
     * training.<br>
     * <i>numberOfSamplesToBeTested</i> represents the actual number of samples that will
     * be used for training and it is always substracted by the <i>windowsLength</i>. So, if
     * <i>numberOfSamplesToBeTested</i> is 500 then  500 - <i>windowsLength</i> samples
     * will be used for testing.<br> If <i>numberOfSamplesToBeTested</i> is -1, number of all
     * samples - <i>windowsLength</i> will be used for testing.
     *
     * @param file                      dataset file.
     * @param windowsLength             neural network parameter.
     * @param numberOfSamplesToBeTested optional number of samples to be tested.
     * @throws IOException if error occurs while reading from provided <i>file</i>.
     */
    void loadDataset(String file, int windowsLength, int numberOfSamplesToBeTested) throws IOException;

    /**
     * This methods delegates {@link #loadDataset(String, int, int)} where windowsLength is set
     * to 1.
     *
     * @param file                      dataset file.
     * @param numberOfSamplesToBeTested optional number of samples to be tested.
     * @throws IOException if error occurs while reading from provided <i>file</i>.
     */
    void loadDataset(String file, int numberOfSamplesToBeTested) throws IOException;

    /**
     * Transforms dataset into values between <i>a</i> and <i>b</i> both inclusive where
     * minimum value from dataset is mapped to <i>a</i> and maximum value to <i>b</i>.
     *
     * @param a minimum map value.
     * @param b maximum map value.
     */
    void transformValues(double a, double b);

    /**
     * @return number of samples to be tested.
     */
    int numberOfSamplesToBeTested();

    /**
     * Resets the inner counter for {@link #next()} method.
     */
    void resetCounter();

}
