package hr.fer.zemris.optjava.dz5.part1.function;

import hr.fer.zemris.optjava.dz5.part1.model.BitVector;

public class FunctionImpl implements IFunction<BitVector> {

    @Override
    public double valueAt(BitVector point) {
        int k = point.numberOfSetBits();
        int n = point.size();
        double lowerBound = 0.8 * n;
        double upperBound = 0.9 * n;
        if (k <= lowerBound) {
            return (1.0 * k) / n;
        }
        if (k > lowerBound && k <= upperBound) {
            return 0.8;
        }
        return (2.0 * k) / n - 1;
    }

}
