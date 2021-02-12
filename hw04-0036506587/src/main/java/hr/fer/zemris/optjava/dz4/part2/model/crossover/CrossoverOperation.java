package hr.fer.zemris.optjava.dz4.part2.model.crossover;

import hr.fer.zemris.optjava.dz4.part2.algorithm.HGGA;
import hr.fer.zemris.optjava.dz4.part2.model.unit.Box;
import hr.fer.zemris.optjava.dz4.part2.model.gene.Column;
import hr.fer.zemris.optjava.dz4.part2.model.item.Stick;

import java.util.*;

/**
 * An implementation of crossover operation for {@link HGGA}.
 */
public class CrossoverOperation {

    private static final int CROSSOVER_POINTS = 2;

    /**
     * Executes crossover operation on provided <i>parents</i>.
     *
     * @param parents parents.
     * @return an array of children as a result of this operation.
     */
    public static Box crossover(Random rand, Box[] parents) {
        Box[] clonedParents = parents.clone();
        transferGenesBetweenParents(rand, clonedParents);
        return clonedParents[rand.nextInt(clonedParents.length)];
    }

    private static void transferGenesBetweenParents(Random rand, Box[] parents) {
        Box parent1 = parents[0];
        Box parent2 = parents[1];
        int[] crossoverPoints1 = new int[]{6, 8};
        int[] crossoverPoints2 = new int[]{0, 2};
        /*int[] crossoverPoints1 = twoRandomCrossoverPoints(rand, parent1.size());
        int[] crossoverPoints2 = twoRandomCrossoverPoints(rand, parent2.size());*/
        Arrays.sort(crossoverPoints1);
        Arrays.sort(crossoverPoints2);

        // add columns from one to another
        List<Column> columnsToAdd1 = parent1.getColumns(crossoverPoints1[0],
                crossoverPoints1[1]);
        List<Column> columnsToAdd2 = parent2.getColumns(crossoverPoints2[0],
                crossoverPoints2[1]);
        parent1.addColumns(crossoverPoints1[1], columnsToAdd2);
        parent2.addColumns(crossoverPoints2[0], columnsToAdd1);

        // remove duplicates from parent based on new columns
        List<Stick> unassignedSticks1 = parent1.removeDuplicates(crossoverPoints1[1],
                crossoverPoints1[1] + Math.abs(crossoverPoints2[0] - crossoverPoints2[1]));
        List<Stick> unassignedSticks2 = parent2.removeDuplicates(crossoverPoints2[0],
                crossoverPoints2[0] + Math.abs(crossoverPoints1[0] - crossoverPoints1[1]));

        // replace all columns, include unassigned sticks
        unassignedSticks1 = parent1.replacement(unassignedSticks1);
        unassignedSticks2 = parent2.replacement(unassignedSticks2);

        // do first fit descending algorithm
        parent1.firstFitDescending(unassignedSticks1);
        parent2.firstFitDescending(unassignedSticks2);
    }

    /**
     * Randomly generates an array of two crossover points. Those points will be unique and
     * there is no repetitions.
     *
     * @param rand type of {@link Random}.
     * @param size size of box.
     * @return two crossover points based on <i>size</i>.
     */
    private static int[] twoRandomCrossoverPoints(Random rand, int size) {
        int[] crossoverPoints = new int[CROSSOVER_POINTS];
        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i <= size; i++) {
            indexes.add(i);
        }
        for (int i = 0; i < CROSSOVER_POINTS; i++) {
            crossoverPoints[i] = indexes.remove(rand.nextInt(size));
        }
        return crossoverPoints;
    }
}
