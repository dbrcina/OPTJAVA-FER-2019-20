package hr.fer.zemris.optjava.dz5.part2.model.crossover;

import hr.fer.zemris.optjava.dz5.part2.model.Unit;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.Random;

public class RandomCrossover implements ICrossover<Unit> {

    private Random rand;

    public RandomCrossover(Random rand) {
        this.rand = rand;
    }

    @Override
    public Unit crossover(Unit parent1, Unit parent2) {
        int n = parent1.dimension();
        RealMatrix childDistances = new Array2DRowRealMatrix(n, n);
        RealMatrix childGoods = new Array2DRowRealMatrix(n, n);
        int[] childPermutation = new int[n];
        RealMatrix parent1Distances = parent1.getDistances();
        RealMatrix parent1Goods = parent1.getGoods();
        int[] parent1Permutation = parent1.getPermutation();
        RealMatrix parent2Distances = parent2.getDistances();
        RealMatrix parent2Goods = parent2.getGoods();
        int[] parent2Permutation = parent2.getPermutation();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                double distance = rand.nextDouble() <= 0.5 ?
                        parent1Distances.getEntry(i, j) :
                        parent2Distances.getEntry(i, j);
                double good = rand.nextDouble() <= 0.5 ?
                        parent1Goods.getEntry(i, j) :
                        parent2Goods.getEntry(i, j);
                childDistances.setEntry(i, j, distance);
                childGoods.setEntry(i, j, good);
            }
            childPermutation[i] = rand.nextDouble() <= 0.5 ?
                    parent1Permutation[i] :
                    parent2Permutation[i];
        }
        Unit child = new Unit(childDistances, childGoods);
        child.setPermutation(childPermutation);
        return child;
    }

}
