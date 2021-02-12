package hr.fer.zemris.optjava.dz4.part2.model;

import hr.fer.zemris.optjava.dz4.part2.model.unit.Box;
import hr.fer.zemris.optjava.dz4.part2.model.gene.Column;
import hr.fer.zemris.optjava.dz4.part2.model.item.Stick;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Model of population. Every population consists of list of boxes modeled by {@link Box}
 * type.
 */
public class Population {

    private List<Box> boxes;
    private int size;

    public List<Box> getBoxes() {
        return boxes;
    }

    public Box getBestBox() {
        return boxes.stream()
                .sorted()
                .findFirst()
                .get();
    }

    public Population(int size) {
        this.size = size;
        this.boxes = new ArrayList<>(size);
    }

    /**
     * Replaces <i>oldBox</i> with <i>newBox</i>.
     *
     * @param oldBox new box.
     * @param newBox old box.
     * @throws IllegalArgumentException if old box does not exist in population.
     */
    public void replace(Box newBox, Box oldBox) {
        int index = boxes.indexOf(oldBox);
        if (index == -1) {
            throw new IllegalArgumentException("Old box does not exist");
        }
        boxes.remove(index);
        boxes.add(index, newBox);
    }

    /**
     * Randomizes initial population with <i>loadedSticks</i>, a list of sticks.
     *
     * @param loadedSticks list of sticks.
     * @param height       height of a box/column.
     */
    public void randomize(List<Stick> loadedSticks, int height) {
        for (int i = 0; i < size; i++) {
            List<Stick> temp = new ArrayList<>(loadedSticks);
            Box box = new Box();
            for (int j = 0; j < loadedSticks.size(); j++) {
                Column column = new Column(height);
                List<Integer> sticksToRemove = new ArrayList<>();
                for (int k = 0; k < temp.size(); k++) {
                    Stick stick = temp.get(k);
                    if (!column.addStick(stick)) break;
                    sticksToRemove.add(k);
                }
                box.addColumn(column);
                Collections.reverse(sticksToRemove);
                for (int index : sticksToRemove) {
                    temp.remove(index);
                }
                if (temp.isEmpty()) break;
            }
            boxes.add(box);
            Collections.shuffle(loadedSticks);
        }
    }
}
