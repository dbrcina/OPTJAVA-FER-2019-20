package hr.fer.zemris.optjava.dz12.ga.evaluator;

import hr.fer.zemris.optjava.dz12.engine.GPEngine;
import hr.fer.zemris.optjava.dz12.ga.solution.Solution;
import hr.fer.zemris.optjava.dz12.ga.treenode.FunctionNode;

public class Evaluator {

    private GPEngine engine;

    public Evaluator(GPEngine engine) {
        this.engine = engine;
    }

    public void evaluate(Solution solution) {
        FunctionNode root = solution.getRoot();
        while (engine.hasMoreActions()) {
            root.applyFunction(engine);
        }
        solution.setFitness(engine.getFoodAte());
        engine.reset();
    }

}
