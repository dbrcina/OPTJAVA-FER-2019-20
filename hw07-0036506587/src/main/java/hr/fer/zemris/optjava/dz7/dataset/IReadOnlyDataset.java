package hr.fer.zemris.optjava.dz7.dataset;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map.Entry;

public interface IReadOnlyDataset {

    void loadDataset(Path file) throws IOException;

    int numberOfSamples();

    Entry<double[], double[]> inputsOutputsPair(int position);

}
