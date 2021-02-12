package hr.fer.zemris.optjava.dz12.ga.treenode;

import hr.fer.zemris.optjava.dz12.engine.GPEngine;

import java.util.StringJoiner;

public class IfFoodAhead extends FunctionNode {

    public IfFoodAhead(int depth) {
        super(depth);
    }

    private IfFoodAhead() {
    }

    @Override
    public FunctionNode copy() {
        return copyNode(new IfFoodAhead());
    }

    @Override
    public void applyFunction(GPEngine engine) {
        if (engine.isFoodAhead()) {
            children.get(0).applyFunction(engine);
        } else {
            children.get(1).applyFunction(engine);
        }
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner(", ");
        children.forEach(child -> sj.add(child.toString()));
        return "IfFoodAhead(" + sj.toString() + ")";
    }

}
