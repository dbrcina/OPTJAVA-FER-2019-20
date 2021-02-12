package hr.fer.zemris.optjava.dz12.ga.solution;

import hr.fer.zemris.optjava.dz12.ga.treenode.FunctionNode;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Comparator;

public class Solution implements Comparable<Solution> {

    private FunctionNode root;
    private int fitness;

    public Solution(FunctionNode root) {
        this.root = root;
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    public FunctionNode getRoot() {
        return root;
    }

    public void punishPlagiarism() {
        setFitness((int) (fitness * 0.9));
    }

    public void writeSolutionProgram(Path path) throws IOException {

    }

    @Override
    public String toString() {
        return root.toString();
    }

    @Override
    public int compareTo(Solution other) {
        return Comparator.comparingInt(Solution::getFitness).reversed()
                .thenComparing(s -> s.root.numberOfNodesBelow())
                .compare(this, other);
    }

}
