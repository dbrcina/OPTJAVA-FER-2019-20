package hr.fer.zemris.optjava.dz12.ga.treenode;

import hr.fer.zemris.optjava.dz12.engine.GPEngine;

public class Move extends FunctionNode {

    public Move(int depth) {
        super(depth);
    }

    private Move() {
    }

    @Override
    public void applyFunction(GPEngine engine) {
        engine.moveFunction();
    }

    @Override
    public boolean addChild(FunctionNode child) {
        throw new RuntimeException("Node cannot have any children");
    }

    @Override
    public FunctionNode copy() {
        return copyNode(new Move());
    }

    @Override
    public String toString() {
        return "Move";
    }

}
