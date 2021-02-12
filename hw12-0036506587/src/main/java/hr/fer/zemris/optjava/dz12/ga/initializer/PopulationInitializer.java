package hr.fer.zemris.optjava.dz12.ga.initializer;

import hr.fer.zemris.optjava.dz12.ga.solution.Solution;
import hr.fer.zemris.optjava.dz12.ga.treenode.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PopulationInitializer {

    private Random rand;

    public PopulationInitializer(Random rand) {
        this.rand = rand;
    }

    /**
     * Initializes population with <strong>ramped-half-and-half</strong> method.
     *
     * @param popSize  population size.
     * @param minDepth min depth for initialization method.
     * @param maxDepth max depth for initialization method.
     * @return new population.
     */
    public List<Solution> initialize(int popSize, int minDepth, int maxDepth) {
        List<Solution> population = new ArrayList<>();
        int numOfDifferentDepths = maxDepth - minDepth + 1;
        double numOfTreesWithCertainDepth = popSize * 1.0 / numOfDifferentDepths;
        int currentDepth = minDepth;
        for (int i = 0, temp = 0; i < popSize; i++) {
            // check for full method
            if (temp < numOfTreesWithCertainDepth / 2)
                population.add(fullSolution(currentDepth));
            else {
                // check for grow method
                if (temp < numOfTreesWithCertainDepth)
                    population.add(growSolution(currentDepth));
                else {
                    temp = -1;
                    i--;
                    currentDepth++;
                }
            }
            temp++;
        }
        return population;
    }

    /**
     * Creates one new solution with full method.
     *
     * @param maxDepth max depth.
     * @return solution.
     */
    public Solution fullSolution(int maxDepth) {
        FunctionNode root = fullFunctionNode(0, maxDepth);
        return new Solution(root);
    }

    /**
     * Creates one new solution with grow method.
     *
     * @param maxDepth max depth.
     * @return solution.
     */
    public Solution growSolution(int maxDepth) {
        FunctionNode root = growFunctionNode(0, maxDepth);
        return new Solution(root);
    }

    /**
     * Generates a new subtree with full method.
     *
     * @param currentDepth current depth.
     * @param maxDepth     max depth.
     * @return new subtree.
     */
    public FunctionNode fullFunctionNode(int currentDepth, int maxDepth) {
        if (currentDepth == maxDepth) return newTerminalNode(currentDepth);
        return newFunctionNode(currentDepth, maxDepth, true);
    }

    /**
     * Generates a new subtree with grow method.
     *
     * @param currentDepth current depth.
     * @param maxDepth     max depth.
     * @return new subtree.
     */
    public FunctionNode growFunctionNode(int currentDepth, int maxDepth) {
        // root..
        if (currentDepth == 0) return newFunctionNode(currentDepth, maxDepth, false);
        // terminals...
        if (currentDepth == maxDepth) return newTerminalNode(currentDepth);
        switch (rand.nextInt(6)) {
            case 0:
            case 1:
            case 2:
                return newTerminalNode(currentDepth);
            case 3:
            case 4:
            case 5:
            default:
                return newFunctionNode(currentDepth, maxDepth, false);
        }
    }

    // helper method used for generating new function for full or grow method
    private FunctionNode newFunctionNode(int currentDepth, int maxDepth, boolean full) {
        FunctionNode node;
        switch (rand.nextInt(3)) {
            case 0:
                node = new IfFoodAhead(currentDepth);
                break;
            case 1:
                node = new Prog2(currentDepth);
                break;
            case 2:
            default:
                node = new Prog3(currentDepth);
        }
        node.addChild(full
                ? fullFunctionNode(currentDepth + 1, maxDepth)
                : growFunctionNode(currentDepth + 1, maxDepth)
        );
        node.addChild(full
                ? fullFunctionNode(currentDepth + 1, maxDepth)
                : growFunctionNode(currentDepth + 1, maxDepth)
        );
        if (node instanceof Prog3)
            node.addChild(full
                    ? fullFunctionNode(currentDepth + 1, maxDepth)
                    : growFunctionNode(currentDepth + 1, maxDepth)
            );
        return node;
    }

    // helper method used for generating new terminal node
    private FunctionNode newTerminalNode(int currentDepth) {
        switch (rand.nextInt(3)) {
            case 0:
                return new Move(currentDepth);
            case 1:
                return new Left(currentDepth);
            case 2:
            default:
                return new Right(currentDepth);
        }
    }

}
