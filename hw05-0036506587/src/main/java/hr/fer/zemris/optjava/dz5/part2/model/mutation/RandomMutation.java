package hr.fer.zemris.optjava.dz5.part2.model.mutation;

import hr.fer.zemris.optjava.dz5.part2.model.Unit;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.Random;

public class RandomMutation implements IMutation<Unit> {

    private Random rand;

    public RandomMutation(Random rand) {
        this.rand = rand;
    }

    @Override
    public void mutate(Unit child) {
        int n = child.dimension();
        RealMatrix distances = child.getDistances();
        RealMatrix goods = child.getGoods();
        int[] rows = {rand.nextInt(n), rand.nextInt(n)};
        int[] columns = {rand.nextInt(n), rand.nextInt(n)};

        double temp = distances.getEntry(rows[0], columns[0]);
        distances.setEntry(rows[0], columns[0], distances.getEntry(rows[1], columns[1]));
        distances.setEntry(rows[1], columns[1], temp);

        temp = goods.getEntry(rows[0], columns[0]);
        goods.setEntry(rows[0], columns[0], goods.getEntry(rows[1], columns[1]));
        goods.setEntry(rows[1], columns[1], temp);
    }

}
