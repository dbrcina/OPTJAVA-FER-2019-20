package hr.fer.zemris.optjava.dz3.decoder;

import hr.fer.zemris.optjava.dz3.Util;
import hr.fer.zemris.optjava.dz3.solution.BitvectorSolution;

import java.util.Arrays;

public class NaturalBinaryDecoder extends BitvectorDecoder {

    public NaturalBinaryDecoder(double[] mins, double[] maxs, int[] bits, int n) {
        super(mins, maxs, bits, n);
    }

    public NaturalBinaryDecoder(double min, double max, int bit, int n) {
        super(min, max, bit, n);
    }

    @Override
    public double[] decode(BitvectorSolution solution) {
        double[] decoded = new double[n];
        decode(solution, decoded);
        return decoded;
    }

    @Override
    public void decode(BitvectorSolution solution, double[] values) {
        int lenToSkip = 0;
        for (int i = 0; i < n; i++) {
            boolean[] currentVar = Arrays.copyOfRange(
                    solution.bits,
                    lenToSkip,
                    lenToSkip + bits[i]
            );
            String representation = Util.stringFromBooleanArray(currentVar);
            int k = Integer.parseInt(representation, 2);
            values[i] = mins[i] + k / (Math.pow(2, bits[i]) - 1) * (maxs[i] - mins[i]);
            lenToSkip += bits[i];
        }
    }
}
