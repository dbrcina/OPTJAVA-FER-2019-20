package hr.fer.zemris.optjava.dz3.decoder;

import hr.fer.zemris.optjava.dz3.solution.DoubleArraySolution;

/**
 * An implementation of {@link IDecoder} which basically does nothing but passes solution
 * without decoding anything.
 */
public class PassThroughDecoder implements IDecoder<DoubleArraySolution> {

    @Override
    public double[] decode(DoubleArraySolution solution) {
        double[] values = new double[solution.values.length];
        decode(solution, values);
        return values;
    }

    @Override
    public void decode(DoubleArraySolution solution, double[] values) {
        System.arraycopy(solution.values, 0, values, 0, values.length);
    }
}
