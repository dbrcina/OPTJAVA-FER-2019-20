package hr.fer.zemris.generic.ga.evaluator;

import hr.fer.zemris.generic.ga.solution.GASolution;

public interface IGAEvaluator<T> {

    void evaluate(GASolution<T> p);

}
