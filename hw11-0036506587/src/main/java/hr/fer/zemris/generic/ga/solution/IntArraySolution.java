package hr.fer.zemris.generic.ga.solution;

public class IntArraySolution extends GASolution<int[]> {

    public IntArraySolution(int[] data) {
        this(data, 0);
    }

    public IntArraySolution(int[] data, double fitness) {
        this.data = data == null ? null : data.clone();
        this.fitness = fitness;
    }

    @Override
    public GASolution<int[]> duplicate() {
        return new IntArraySolution(data, fitness);
    }

}
