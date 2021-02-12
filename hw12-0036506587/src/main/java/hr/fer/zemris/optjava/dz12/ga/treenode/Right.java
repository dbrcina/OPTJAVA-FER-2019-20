package hr.fer.zemris.optjava.dz12.ga.treenode;

import hr.fer.zemris.optjava.dz12.engine.GPEngine;

public class Right extends FunctionNode {

    public Right(int depth) {
        super(depth);
    }

    private Right() {
    }

    @Override
    public void applyFunction(GPEngine engine) {
        engine.rightFunction();
    }

    @Override
    public boolean addChild(FunctionNode child) {
        throw new RuntimeException("Node cannot have any children");
    }

    @Override
    public FunctionNode copy() {
        return copyNode(new Right());
    }

    @Override
    public String toString() {
        return "Right";
    }

}
