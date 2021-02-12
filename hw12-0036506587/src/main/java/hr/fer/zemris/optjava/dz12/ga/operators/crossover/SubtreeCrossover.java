package hr.fer.zemris.optjava.dz12.ga.operators.crossover;

import hr.fer.zemris.optjava.dz12.ga.evaluator.Evaluator;
import hr.fer.zemris.optjava.dz12.ga.solution.Solution;
import hr.fer.zemris.optjava.dz12.ga.treenode.FunctionNode;

import java.util.Random;

public class SubtreeCrossover {

    private Random rand;
    private Evaluator evaluator;

    public SubtreeCrossover(Random rand, Evaluator evaluator) {
        this.rand = rand;
        this.evaluator = evaluator;
    }

    public Solution crossover(Solution p1, Solution p2) {
        FunctionNode root1 = p1.getRoot().copy();
        FunctionNode root2 = p2.getRoot().copy();

        // parent 1
        int numberOfNodesBelowP1 = root1.numberOfNodesBelow();
        int randomNodeNumber = rand.nextInt(numberOfNodesBelowP1) + 1;
        FunctionNode subtree1 = root1.getSubtreeNode(randomNodeNumber);

        // parent 2
        int numberOfNodesBelowP2 = root2.numberOfNodesBelow();
        randomNodeNumber = rand.nextInt(numberOfNodesBelowP2) + 1;
        FunctionNode subtree2 = root2.getSubtreeNode(randomNodeNumber);

        subtree1.replaceBy(subtree2);

        FunctionNode result;
        if (root1.numberOfNodesBelow() + 1 < 200) {
            result = root1;
        } else {
            result = p1.getFitness() > p2.getFitness() ? p1.getRoot() : p2.getRoot();
        }

        Solution solution = new Solution(result);
        evaluator.evaluate(solution);
        if (solution.getFitness() == p1.getFitness()) {
            solution.punishPlagiarism();
        }
        return solution;
    }

}
