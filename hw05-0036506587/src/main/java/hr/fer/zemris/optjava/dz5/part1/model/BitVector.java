package hr.fer.zemris.optjava.dz5.part1.model;

import java.util.Arrays;
import java.util.Random;
import java.util.StringJoiner;

public class BitVector {

    private boolean[] bits;

    public BitVector(int n) {
        this.bits = new boolean[n];
    }

    public void set(int index, boolean value) {
        bits[index] = value;
    }

    public boolean get(int index) {
        return bits[index];
    }

    public int numberOfSetBits() {
        int k = 0;
        for (boolean bit : bits) {
            if (bit) k++;
        }
        return k;
    }

    public int size() {
        return bits.length;
    }

    public void randomize(Random rand) {
        for (int i = 0; i < size(); i++) {
            bits[i] = rand.nextBoolean();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BitVector bitVector = (BitVector) o;
        return Arrays.equals(bits, bitVector.bits);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(bits);
    }

    @Override
    public String toString() {
        return Integer.toString(numberOfSetBits());
    }

}
