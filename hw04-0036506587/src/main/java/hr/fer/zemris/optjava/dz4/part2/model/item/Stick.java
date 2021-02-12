package hr.fer.zemris.optjava.dz4.part2.model.item;

import java.util.Objects;

/**
 * Simple model of a stick. Every stick has its own length which is a type of {@link Integer}.
 */
public class Stick {

    private int length;

    /**
     * Constructor.
     *
     * @param length stick's length.
     * @throws IllegalArgumentException if length is less than 1.
     */
    public Stick(int length) {
        if (length < 1) {
            throw new IllegalArgumentException("Veličina štapića ne smije biti negativna!");
        }
        this.length = length;
    }

    public int getLength() {
        return length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stick stick = (Stick) o;
        return length == stick.length;
    }

    @Override
    public int hashCode() {
        return Objects.hash(length);
    }

    @Override
    public String toString() {
        return Integer.toString(length);
    }
}
