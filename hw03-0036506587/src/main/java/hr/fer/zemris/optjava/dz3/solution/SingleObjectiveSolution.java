package hr.fer.zemris.optjava.dz3.solution;

/**
 * Simple model of some solution which contains fitness value and solution value.
 */
public class SingleObjectiveSolution implements Comparable<SingleObjectiveSolution> {

    public double fitness;
    public double value;

    @Override
    public int compareTo(SingleObjectiveSolution other) {
        return Double.compare(this.fitness, other.fitness);
    }
}
