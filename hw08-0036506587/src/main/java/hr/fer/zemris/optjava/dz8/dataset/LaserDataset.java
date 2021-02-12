package hr.fer.zemris.optjava.dz8.dataset;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.NoSuchElementException;

/**
 * An implementation of {@link IReadOnlyDataset} interface.
 */
public class LaserDataset implements IReadOnlyDataset {

    private double[] samples;
    private int numberOfSamplesToBeTested;
    private int counter;

    @Override
    public void loadDataset(
            String file,
            int windowsLength,
            int numberOfSamplesToBeTested) throws IOException {
        loadDataset(file);
        this.numberOfSamplesToBeTested = numberOfSamplesToBeTested == -1 ?
                samples.length - windowsLength :
                numberOfSamplesToBeTested - windowsLength;
    }

    @Override
    public void loadDataset(String file, int numberOfSamplesToBeTested) throws IOException {
        loadDataset(file, 1, numberOfSamplesToBeTested);
    }

    /**
     * Helper method used for reading from a <i>file</i>.
     *
     * @param file file.
     * @throws IOException if error occurs while reading from a file.
     */
    private void loadDataset(String file) throws IOException {
        samples = Files.readAllLines(Paths.get(file))
                .stream()
                .map(String::trim)
                .mapToDouble(Double::parseDouble)
                .toArray();
    }

    @Override
    public void transformValues(double a, double b) {
        double[] minAndMax = minAndMax();
        for (int i = 0; i < samples.length; i++) {
            samples[i] = (b - a) *
                    (samples[i] - minAndMax[0]) /
                    (minAndMax[1] - minAndMax[0])
                    - 1;
        }
    }

    private double[] minAndMax() {
        double min = samples[0];
        double max = samples[0];
        for (double sample : samples) {
            if (sample < min) min = sample;
            if (sample > max) max = sample;
        }
        return new double[]{min, max};
    }

    @Override
    public int numberOfSamplesToBeTested() {
        return numberOfSamplesToBeTested;
    }

    @Override
    public boolean hasNext() {
        return counter != numberOfSamplesToBeTested;
    }

    @Override
    public Double next() {
        if (!hasNext()) throw new NoSuchElementException();
        return samples[counter++];
    }

    @Override
    public void resetCounter() {
        counter = 0;
    }

}
