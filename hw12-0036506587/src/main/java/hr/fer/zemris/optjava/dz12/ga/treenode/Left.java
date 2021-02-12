package hr.fer.zemris.optjava.dz12.ga.treenode;

import hr.fer.zemris.optjava.dz12.engine.GPEngine;

public class Left extends FunctionNode {

    public Left(int depth) {
        super(depth);
    }

    private Left() {
    }

    @Override
    public void applyFunction(GPEngine engine) {
        engine.leftFunction();
    }

    @Override
    public boolean addChild(FunctionNode child) {
        throw new RuntimeException("Node cannot have any children");
    }

    @Override
    public FunctionNode copy() {
        return copyNode(new Left());
    }

    @Override
    public String toString() {
        return "Left";
    }

}
