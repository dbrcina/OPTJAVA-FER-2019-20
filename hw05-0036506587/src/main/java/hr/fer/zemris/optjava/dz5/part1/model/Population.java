package hr.fer.zemris.optjava.dz5.part1.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Population {

    private List<BitVector> units = new ArrayList<>();
    private int size;

    public Population(int size) {
        this.size = size;
    }

    public Population() {
        this(0);
    }

    public int size() {
        return size;
    }

    public List<BitVector> getUnits() {
        return units;
    }

    public void add(BitVector unit) {
        if (units.contains(unit)) return;
        units.add(unit);
        size++;
    }

    public BitVector get(int index) {
        return units.get(index);
    }

    public BitVector getRandomUnit(Random rand) {
        return units.get(rand.nextInt(size()));
    }

    public void randomize(Random rand, int n) {
        for (int i = 0; i < size; i++) {
            BitVector unit = new BitVector(n);
            unit.randomize(rand);
            units.add(unit);
        }
    }
}
