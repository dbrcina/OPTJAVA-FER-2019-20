package hr.fer.zemris.optjava.dz4.part2.model.selection;

import hr.fer.zemris.optjava.dz4.part2.model.Population;
import hr.fer.zemris.optjava.dz4.part2.model.unit.Box;

import java.util.*;

/**
 * An implementation of <i>n-tournament</i> selection.
 */
public class NTournamentSelection {

    /**
     * Executes selection on provided <i>population</i>.
     *
     * @param rand       type of {@link Random}.
     * @param population population.
     * @param n          parameter n for selection.
     * @param best       boolean flag.
     * @return best result if flag <i>best</i> is <i>true</i>, otherwise worst result.
     */
    public static Box selection(Random rand, Population population, int n, boolean best) {
        List<Box> boxes = population.getBoxes();
        List<Box> selectedBoxes = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Box selected = boxes.get(rand.nextInt(boxes.size()));
            selectedBoxes.add(selected);
        }

        // boxes are sorted in ascending order by their size,
        // so first element is the best,
        // therefore the last is the worst
        selectedBoxes.sort(Box::compareTo);

        Box result = null;
        Iterator<Box> iterator = selectedBoxes.iterator();
        if (best) {
            result = iterator.next();
        } else {
            while (iterator.hasNext()) {
                result = iterator.next();
            }
        }
        return result;
    }
}
