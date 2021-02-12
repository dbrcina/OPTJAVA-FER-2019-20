package hr.fer.zemris.optjava.dz4.part2.model.unit;

import hr.fer.zemris.optjava.dz4.part2.model.gene.Column;
import hr.fer.zemris.optjava.dz4.part2.model.item.Stick;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Box represents one unit in a population. Every box consists of columns modeled by {@link
 * Column} type.<br> It also implements {@link Comparable} interface which means that boxes
 * know how to compare themselves based on their lenght in ascending order.
 */
public class Box implements Comparable<Box> {

    private List<Column> columns = new ArrayList<>();

    /**
     * @param fromIndex index from.
     * @param toIndex   index to.
     * @return list of columns from <i>fromIndex</i>(inclusive) to <i>toIndex</i>(exclusive).
     */
    public List<Column> getColumns(int fromIndex, int toIndex) {
        List<Column> temp = columns.subList(fromIndex, toIndex);
        return temp.stream().map(Column::copy).collect(Collectors.toList());
    }

    /**
     * Destroys two columns if possible and returns list of sticks that were deleted with those
     * columns.
     *
     * @param rand type of {@link Random}.
     * @return list of unassignedSticks.
     * @see <a href="https://www.codeproject.com/Articles/633133/ga-bin-packing">
     * https://www.codeproject.com/Articles/633133/ga-bin-packing</a>
     */
    public List<Stick> destroyColumns(Random rand) {
        int index = rand.nextInt(columns.size());
        List<Stick> unassignedSticks = new ArrayList<>(columns.remove(index).getSticks());
        if (columns.size() != 0) {
            index = rand.nextInt(columns.size());
            unassignedSticks.addAll(columns.remove(index).getSticks());
        }
        return unassignedSticks;
    }

    /**
     * Sorts provided list of sticks in descending order by their lenght and places them in
     * first fitting column or creates a new column if there is no place for one.
     *
     * @param sticks list of sticks.
     * @see <a href="https://www.codeproject.com/Articles/633133/ga-bin-packing">
     * https://www.codeproject.com/Articles/633133/ga-bin-packing</a>
     */
    public void firstFitDescending(List<Stick> sticks) {
        sticks.sort((s1, s2) -> Integer.compare(s2.getLength(),
                s1.getLength()));
        for (Stick stick : sticks) {
            boolean addded = false;
            for (Column column : columns) {
                addded = column.addStick(stick);
                if (addded) break;
            }
            if (!addded) {
                Column newColumn = new Column(columns.get(0).getHeight());
                newColumn.addStick(stick);
                columns.add(newColumn);
            }
        }
    }

    /**
     * Removes duplicates from <b>this</b> box based on new columns that were added from
     * another parent and returns a list of unassigned sticks that were removed as a
     * <i>"collateral damage"</i>.
     *
     * @param fromIndex index from.
     * @param toIndex   index to.
     * @return list of unassigned sticks.
     * @see <a href="https://www.codeproject.com/Articles/633133/ga-bin-packing">
     * https://www.codeproject.com/Articles/633133/ga-bin-packing</a>
     */
    public List<Stick> removeDuplicates(int fromIndex, int toIndex) {
        List<Column> newColumns = getColumns(fromIndex, toIndex);
        List<Stick> newSticks = new ArrayList<>();
        for (Column column : newColumns) {
            newSticks.addAll(column.getSticks());
        }
        List<Stick> unassignedSticks = new ArrayList<>();
        List<Integer> columnsToRemove = new ArrayList<>();
        for (int i = 0; i < size(); i++) {
            if (i >= fromIndex && i < toIndex) continue;
            Column column = columns.get(i);
            List<Stick> currentSticks = column.getSticks();
            if (currentSticks.stream().anyMatch(newSticks::contains)) {
                unassignedSticks.addAll(currentSticks.stream()
                        .filter(s -> !newSticks.contains(s))
                        .collect(Collectors.toList()));
                newSticks.removeAll(currentSticks);
                columnsToRemove.add(i);
            }
        }
        Collections.reverse(columnsToRemove);
        for (int index : columnsToRemove) {
            columns.remove(index);
        }
        return unassignedSticks;
    }

    /**
     * @param sticks list of sticks.
     * @return list of unassigned sticks.
     * @see <a href="https://www.codeproject.com/Articles/633133/ga-bin-packing">
     * https://www.codeproject.com/Articles/633133/ga-bin-packing</a>
     */
    public List<Stick> replacement(List<Stick> sticks) {
        if (sticks.isEmpty()) return new ArrayList<>();
        List<Stick> unassignedSticks = new ArrayList<>();
        for (Stick stick : sticks) {
            boolean added = false;
            for (Column column : columns) {
                //if (column.getFill() == column.getHeight()) continue;
                List<Stick> sticksToRemove = removeSticks(stick, column.copy());
                if (sticksToRemove.isEmpty()) {
                    continue;
                }
                unassignedSticks.addAll(sticksToRemove);
                sticksToRemove.forEach(column::removeStick);
                added = column.addStick(stick);
                break;
            }
            if (!added) unassignedSticks.add(stick);
        }
        return unassignedSticks;
    }

    /**
     * Recursion for {@link #replacement(List)} method.
     *
     * @param stick  stick to compare with.
     * @param column column to test.
     * @return list of sticks to remove.
     * @see <a href="https://www.codeproject.com/Articles/633133/ga-bin-packing">
     * https://www.codeproject.com/Articles/633133/ga-bin-packing</a>
     */
    private List<Stick> removeSticks(Stick stick, Column column) {
        if (column.getFill() == 0) return new ArrayList<>();
        List<Stick> sticksToRemove = new ArrayList<>();
        int sizeU = stick.getLength();
        int sizeB = column.getFill();
        int sizeP = column.getSticks().stream().mapToInt(Stick::getLength).sum();
        if (sizeU > sizeP && sizeB - sizeP + sizeU <= column.getHeight()) {
            sticksToRemove.addAll(column.getSticks());
        }
        if (sticksToRemove.isEmpty()) {
            Column temp1 = column.copy();
            temp1.removeStick(column.getSticks().stream()
                    .max(Comparator.comparingInt(Stick::getLength))
                    .get()
            );
            List<Stick> tempList1 = removeSticks(stick, temp1);
            Column temp2 = column.copy();
            temp2.removeStick(column.getSticks().stream()
                    .min(Comparator.comparingInt(Stick::getLength))
                    .get()
            );
            List<Stick> tempList2 = removeSticks(stick, temp2);
            int sum1 = tempList1.stream().mapToInt(Stick::getLength).sum();
            int sum2 = tempList2.stream().mapToInt(Stick::getLength).sum();
            boolean predicate = sum1 > sum2 && sum2 == 0
                    || sum1 < sum2 && sum2 + stick.getLength() < column.getHeight();
            sticksToRemove.addAll(predicate ? tempList1 : tempList2);
        }
        return sticksToRemove;
    }

    public int size() {
        return columns.size();
    }

    /**
     * Adds list of <i>columns</i> to <b>this</b> box from position <i>fromIndex</i>.
     *
     * @param fromIndex index from.
     * @param columns   list of columns.
     */
    public void addColumns(int fromIndex, List<Column> columns) {
        this.columns.addAll(fromIndex, columns);
    }

    public void addColumn(Column column) {
        addColumns(size(), Arrays.asList(column));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size(); i++) {
            sb.append(i + 1).append(". stupac:").append(columns.get(i)).append("\n");
        }
        return sb.toString();
    }

    @Override
    public int compareTo(Box other) {
        return Integer.compare(this.size(), other.size());
    }
}
