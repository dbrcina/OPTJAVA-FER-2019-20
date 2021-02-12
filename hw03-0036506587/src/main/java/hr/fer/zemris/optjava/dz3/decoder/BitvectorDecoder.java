package hr.fer.zemris.optjava.dz3.decoder;

import hr.fer.zemris.optjava.dz3.solution.BitvectorSolution;

import java.util.Arrays;

public abstract class BitvectorDecoder implements IDecoder<BitvectorSolution> {

    protected double[] mins;
    protected double[] maxs;
    protected int[] bits;
    protected int n;
    protected int totalBits;

    public BitvectorDecoder(double[] mins, double[] maxs, int[] bits, int n) {
        this.mins = mins;
        this.maxs = maxs;
        this.bits = bits;
        this.n = n;
        this.totalBits = Arrays.stream(bits).sum();
    }

    public BitvectorDecoder(double min, double max, int bit, int n) {
        this.mins = new double[n];
        Arrays.fill(mins, min);
        this.maxs = new double[n];
        Arrays.fill(maxs, max);
        this.bits = new int[n];
        Arrays.fill(bits, bit);
        this.n = n;
        this.totalBits = n * bit;
    }

    public int getTotalBits() {
        return totalBits;
    }

    public int getDimensions() {
        return n;
    }
}
