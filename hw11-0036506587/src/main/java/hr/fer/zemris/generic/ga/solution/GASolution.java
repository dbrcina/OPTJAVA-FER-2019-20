package hr.fer.zemris.generic.ga.solution;

import java.util.Objects;

public abstract class GASolution<T> implements Comparable<GASolution<T>> {

    protected T data;

    public double fitness;

    public T getData() {
        return data;
    }

    public abstract GASolution<T> duplicate();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GASolution)) return false;
        GASolution<?> that = (GASolution<?>) o;
        return data.equals(that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data);
    }

    @Override
    public int compareTo(GASolution<T> other) {
        return -Double.compare(this.fitness, other.fitness);
    }

}
