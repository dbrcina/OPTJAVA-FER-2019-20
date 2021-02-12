package hr.fer.zemris.optjava.dz12.ga.operators.mutation;

import hr.fer.zemris.optjava.dz12.ga.evaluator.Evaluator;
import hr.fer.zemris.optjava.dz12.ga.initializer.PopulationInitializer;
import hr.fer.zemris.optjava.dz12.ga.solution.Solution;
import hr.fer.zemris.optjava.dz12.ga.treenode.FunctionNode;

import java.util.Random;

public class SubtreeMutation {

    private Random rand;
    private Evaluator evaluator;
    private PopulationInitializer initializer;

    public SubtreeMutation(Random rand, Evaluator evaluator, PopulationInitializer initializer) {
        this.rand = rand;
        this.evaluator = evaluator;
        this.initializer = initializer;
    }

    public Solution mutate(Solution parent) {
        FunctionNode root = parent.getRoot().copy();
        int numberOfNodesBelow = root.numberOfNodesBelow();
        // + 1 to skip root
        int randomNodeNumber = rand.nextInt(numberOfNodesBelow) + 1;
        FunctionNode randomNode = root.getSubtreeNode(randomNodeNumber);
        int random = rand.nextInt(10 - randomNode.getDepth()) + randomNode.getDepth();
        FunctionNode newSubtree = rand.nextDouble() < 0.5
                ? initializer.growFunctionNode(randomNode.getDepth(), random)
                : initializer.fullFunctionNode(randomNode.getDepth(), random);
        // check if number of nodes is greater than 200
        // + 1 because of root
        if (numberOfNodesBelow + 1 + newSubtree.numberOfNodesBelow() + 1 > 200) {
            return parent;
        }
        randomNode.replaceBy(newSubtree);

        Solution child = new Solution(root);
        evaluator.evaluate(child);
        if (parent.getFitness() == child.getFitness()) {
            child.punishPlagiarism();
        }
        return child;
    }

}
