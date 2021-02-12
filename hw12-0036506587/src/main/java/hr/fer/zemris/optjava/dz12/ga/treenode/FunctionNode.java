package hr.fer.zemris.optjava.dz12.ga.treenode;

import hr.fer.zemris.optjava.dz12.engine.GPEngine;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple abstract model of a tree node. <br> Each node is defined with tree depth, its parent and
 * children if there are any.
 */
public abstract class FunctionNode {

    protected int depth;
    protected FunctionNode parent;
    protected List<FunctionNode> children;

    protected FunctionNode() {
    }

    public FunctionNode(int depth) {
        this.depth = depth;
    }

    public int getDepth() {
        return depth;
    }

    public boolean addChild(FunctionNode child) {
        if (children == null) children = new ArrayList<>();
        child.parent = this;
        return children.add(child);
    }

    public boolean addAll(FunctionNode... children) {
        for (FunctionNode child : children) {
            if (!addChild(child)) return false;
        }
        return true;
    }

    public void replaceBy(FunctionNode newSubTree) {
        if (parent == null) {
            throw new RuntimeException("Cannot replace root");
        }
        newSubTree.parent = parent;
        newSubTree.depth = depth;
        int index = parent.children.indexOf(this);
        parent.children.remove(index);
        parent.children.add(index, newSubTree);
    }

    /**
     * @return number of nodes below current node.
     */
    public int numberOfNodesBelow() {
        return getAllNodes().size();
    }

    /**
     * Returns a node with provided <i>nodeNumber</i> from subtree.<br> Root node starts with 0 etc.
     *
     * @param nodeNumber node number.
     * @return desired node.
     */
    public FunctionNode getSubtreeNode(int nodeNumber) {
        List<FunctionNode> allNodes = getAllNodes();
        allNodes.add(0, this);
        return allNodes.get(nodeNumber);
    }

    // helper method for finding all nodes
    private List<FunctionNode> getAllNodes() {
        if (children == null) return new ArrayList<>();
        List<FunctionNode> allNodes = new ArrayList<>(children);
        children.forEach(child -> allNodes.addAll(child.getAllNodes()));
        return allNodes;
    }

    // helper method for copying node
    // node received has empty fields
    protected FunctionNode copyNode(FunctionNode node) {
        node.depth = depth;
        if (parent != null) {
            node.parent = parent;
        }
        if (children != null) {
            children.stream().map(FunctionNode::copy).forEach(node::addChild);
        }
        return node;
    }

    public abstract FunctionNode copy();

    public abstract void applyFunction(GPEngine engine);

}
