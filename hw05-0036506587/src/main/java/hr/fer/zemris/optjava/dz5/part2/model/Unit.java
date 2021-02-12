package hr.fer.zemris.optjava.dz5.part2.model;

import hr.fer.zemris.optjava.dz5.part2.Util;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public class Unit implements Comparable<Unit> {

    private RealMatrix distances;
    private RealMatrix goods;
    private int[] permutation;
    private double fitness;

    public Unit(RealMatrix distances, RealMatrix goods) {
        this.distances = distances;
        this.goods = goods;
    }

    public Unit shuffle(Random rand) {
        RealMatrix distances = this.distances.copy();
        RealMatrix goods = this.goods.copy();
        for (int i = 0; i < permutation.length; i++) {
            if (rand.nextDouble() <= 0.5)
                distances.setRow(i, Util.shuffleArray(distances.getRow(i)));
            if (rand.nextDouble() <= 0.5)
                goods.setRow(i, Util.shuffleArray(goods.getRow(i)));
        }
        Unit unit = new Unit(distances, goods);
        int[] permutation = this.permutation.clone();
        unit.setPermutation(Util.shuffleArray(permutation));
        return unit;
    }

    public int dimension() {
        return distances.getRowDimension();
    }

    public RealMatrix getDistances() {
        return distances;
    }

    public RealMatrix getGoods() {
        return goods;
    }

    public int[] getPermutation() {
        return permutation;
    }

    public void setPermutation(int[] permutation) {
        this.permutation = permutation;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Unit unit = (Unit) o;
        return distances.equals(unit.distances) &&
                goods.equals(unit.goods) &&
                Arrays.equals(permutation, unit.permutation);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(distances, goods);
        result = 31 * result + Arrays.hashCode(permutation);
        return result;
    }

    @Override
    public int compareTo(Unit other) {
        return Double.compare(this.fitness, other.fitness);
    }

    @Override
    public String toString() {
        return "fit: " + fitness + ", permutation: " + Arrays.toString(permutation);
    }

}
