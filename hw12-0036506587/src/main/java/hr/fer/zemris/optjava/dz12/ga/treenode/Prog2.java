package hr.fer.zemris.optjava.dz12.ga.treenode;

import hr.fer.zemris.optjava.dz12.engine.GPEngine;

import java.util.StringJoiner;

public class Prog2 extends FunctionNode {

    public Prog2(int depth) {
        super(depth);
    }

    private Prog2() {
    }

    @Override
    public FunctionNode copy() {
        return copyNode(new Prog2());
    }

    @Override
    public void applyFunction(GPEngine engine) {
        children.forEach(child -> child.applyFunction(engine));
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner(", ");
        children.forEach(child -> sj.add(child.toString()));
        return "Prog2(" + sj.toString() + ")";
    }

}
