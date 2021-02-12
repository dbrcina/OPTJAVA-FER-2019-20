package hr.fer.zemris.optjava.dz5.part2.function;

import hr.fer.zemris.optjava.dz5.part2.model.Unit;
import org.apache.commons.math3.linear.RealMatrix;

public class FunctionImpl implements IFunction<Unit> {

    @Override
    public double valutAt(Unit point) {
        double value = 0;
        RealMatrix distances = point.getDistances();
        RealMatrix goods = point.getGoods();
        int[] permutation = point.getPermutation();
        for (int i = 0; i < permutation.length; i++) {
            for (int j = 0; j < permutation.length; j++) {
                value += goods.getEntry(i, j)
                        * distances.getEntry(permutation[i], permutation[j]);
            }
        }
        return value;
    }

}
