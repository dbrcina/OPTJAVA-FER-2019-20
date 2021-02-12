package hr.fer.zemris.optjava.dz4.part2.model.gene;

import hr.fer.zemris.optjava.dz4.part2.model.item.Stick;
import hr.fer.zemris.optjava.dz4.part2.model.unit.Box;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Simple model of one column in a box modeled by {@link Box} type. Every column has fixed
 * height, fill and a list of sticks modeled by {@link Stick} type. <br> It also implements
 * {@link Comparable} interface which means that columns know how to compare themselves based
 * on their fill in descending order.
 */
public class Column implements Comparable<Column> {

    private int height;
    private int fill;
    private List<Stick> sticks = new ArrayList<>();

    public int getHeight() {
        return height;
    }

    public int getFill() {
        return fill;
    }

    public List<Stick> getSticks() {
        return sticks;
    }

    public Column(int height) {
        this.height = height;
    }

    public boolean addStick(Stick stick) {
        int len = stick.getLength();
        if (len > height - fill) return false;
        sticks.add(stick);
        fill += len;
        return true;
    }

    public void removeStick(Stick stick) {
        int len = stick.getLength();
        sticks.remove(stick);
        fill -= len;
    }

    public Column copy() {
        Column column = new Column(height);
        column.fill = fill;
        column.sticks = new ArrayList<>(sticks);
        return column;
    }

    @Override
    public String toString() {
        return sticks.toString();
    }

    @Override
    public int compareTo(Column other) {
        return Integer.compare(other.fill, this.fill);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Column column = (Column) o;
        return sticks.equals(column.sticks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sticks);
    }
}
