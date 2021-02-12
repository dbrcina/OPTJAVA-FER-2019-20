package hr.fer.zemris.optjava.dz4.part1;

import java.util.*;

/**
 * Model of a population which contains a set of units which are modeled by {@link Unit} class.
 * Set of those units is also sorted in descending order based on unit's fitness.
 */
public class Population {

    private final int variablesPerUnit;
    private int sizeOfPopulation;
    private Set<Unit> units;

    /**
     * Constructor.
     *
     * @param variablesPerUnit number of variables per unit.
     * @param sizeOfPopulation size of population.
     */
    public Population(int variablesPerUnit, int sizeOfPopulation) {
        this.variablesPerUnit = variablesPerUnit;
        this.sizeOfPopulation = sizeOfPopulation;
        this.units = new TreeSet<>();
    }

    /**
     * Constructor.
     *
     * @param variablesPerUnit number of variables per unit.
     */
    public Population(int variablesPerUnit) {
        this(variablesPerUnit, 0);
    }

    public Set<Unit> getUnits() {
        return units;
    }

    public Unit getBestUnit() {
        return units.iterator().next();
    }

    public Unit getWorstUnit() {
        Iterator<Unit> it = units.iterator();
        Unit worstUnit = it.next();
        while (it.hasNext()) {
            worstUnit = it.next();
        }
        return worstUnit;
    }

    public boolean addUnit(Unit unit) {
        return addUnits(unit);
    }

    public boolean addUnits(Unit... units) {
        sizeOfPopulation += units.length;
        return this.units.addAll(Arrays.asList(units));
    }

    public Unit[] first(int number) {
        return units.stream()
                .limit(number)
                .toArray(Unit[]::new);
    }

    /**
     * Randomizes <b>this</b> solution based on <i>min</i> and <i>max</i> boundaries.
     *
     * @param rand {@link Random} object.
     * @param min  minimum values.
     * @param max  maximum values.
     * @throws IllegalArgumentException if min and max are wrong dimensions.
     */
    public void randomize(Random rand, double[] min, double[] max) {
        if (min.length != variablesPerUnit || max.length != variablesPerUnit) {
            throw new IllegalArgumentException("Min and max are wrong dimensions");
        }
        for (int i = 0; i < sizeOfPopulation; i++) {
            Unit unit = new Unit();
            unit.values = new double[variablesPerUnit];
            for (int j = 0; j < variablesPerUnit; j++) {
                unit.values[j] += min[j] + rand.nextDouble() * (max[j] - min[j]);
            }
            unit.fitness = GeneticAlgorithm.function.valueAt(unit.values);
            units.add(unit);
        }
    }

    /**
     * Models one unit of a population. A unit has an array of doubles and a fitness value.
     */

    public static class Unit implements Comparable<Unit> {
        private double[] values;
        private double fitness;

        public double[] getValues() {
            return values;
        }

        public void setValues(double[] values) {
            this.values = values;
        }

        public double getFitness() {
            return fitness;
        }

        public void setFitness(double fitness) {
            this.fitness = fitness;
        }

        public void mutate(Random rand, double sigma) {
            for (int i = 0; i < values.length; i++) {
                values[i] += rand.nextGaussian() * sigma;
            }
            fitness = GeneticAlgorithm.function.valueAt(values);
        }

        @Override
        public int compareTo(Unit other) {
            return Double.compare(other.fitness, this.fitness);
        }

        @Override
        public String toString() {
            return Arrays.toString(values) + ", fitness: " + fitness;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Unit unit = (Unit) o;
            return Double.compare(unit.fitness, fitness) == 0 &&
                    Arrays.equals(values, unit.values);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(fitness);
            result = 31 * result + Arrays.hashCode(values);
            return result;
        }
    }
}
