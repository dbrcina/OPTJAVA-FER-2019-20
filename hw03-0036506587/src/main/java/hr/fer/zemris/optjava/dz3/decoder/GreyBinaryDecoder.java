package hr.fer.zemris.optjava.dz3.decoder;

import hr.fer.zemris.optjava.dz3.Util;
import hr.fer.zemris.optjava.dz3.solution.BitvectorSolution;

import java.util.Arrays;

public class GreyBinaryDecoder extends BitvectorDecoder {

    public GreyBinaryDecoder(double[] mins, double[] maxs, int[] bits, int n) {
        super(mins, maxs, bits, n);
    }

    public GreyBinaryDecoder(double min, double max, int bit, int n) {
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
            int k = Integer.parseInt(graytoBinary(representation), 2);
            values[i] = mins[i] + k / (Math.pow(2, bits[i]) - 1) * (maxs[i] - mins[i]);
            lenToSkip += bits[i];
        }
    }

    private String graytoBinary(String gray) {
        StringBuilder binary = new StringBuilder();

        // MSB of binary code is same
        // as gray code
        binary.append(gray.charAt(0));

        // Compute remaining bits
        for (int i = 1; i < gray.length(); i++) {
            // If current bit is 0,
            // concatenate previous bit
            if (gray.charAt(i) == '0')
                binary.append(binary.charAt(i - 1));

                // Else, concatenate invert of
                // previous bit
            else
                binary.append(flip(binary.charAt(i - 1)));
        }

        return binary.toString();
    }

    private char flip(char c) {
        return (c == '0') ? '1' : '0';
    }
}
