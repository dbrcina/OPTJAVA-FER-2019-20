package hr.fer.zemris.optjava.dz4.part2.model.mutation;

import hr.fer.zemris.optjava.dz4.part2.model.unit.Box;
import hr.fer.zemris.optjava.dz4.part2.model.item.Stick;

import java.util.List;
import java.util.Random;

/**
 * An implementation of mutate operation.
 */
public class MutationOperation {

    /**
     * Mutates provided <i>box</i> based on probability of <i>sigma</i>.
     *
     * @param rand  type of {@link Random}.
     * @param box   child.
     * @param sigma sigma.
     */
    public static void mutate(Random rand, Box box, double sigma) {
        if (rand.nextDouble() < sigma) return;
        List<Stick> unassignedSticks = box.destroyColumns(rand);
        unassignedSticks = box.replacement(unassignedSticks);
        box.firstFitDescending(unassignedSticks);
    }
}
