package gui;

import engine.Engine;
import petrinetze.IPetrinet;
import transformation.IRule;

import javax.swing.tree.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: moritz
 * Date: 07.01.11
 * Time: 10:14
 * To change this template use File | Settings | File Templates.
 */
public class PetrinetTreeModel extends DefaultTreeModel {

    public static class Named<T> {

        private String name;

        private T value;

        public Named(String name, T value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Named named = (Named) o;

            if (name != null ? !name.equals(named.name) : named.name != null) return false;
            if (value != null ? !value.equals(named.value) : named.value != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = name != null ? name.hashCode() : 0;
            result = 31 * result + (value != null ? value.hashCode() : 0);
            return result;
        }
    }

    static class RootNode extends DefaultMutableTreeNode {

        RootNode() {
            super("Petrinetze", true);
        }

        public PetrinetNode addPetrinet(String name, Engine engine) {
            final PetrinetNode node = new PetrinetNode(name, engine);
            add(node);
            return node;
        }

        @Override
        public boolean isRoot() {
            return true;
        }
    }

    static class RulesNode extends DefaultMutableTreeNode {

        RulesNode() {
            super("Rules");
        }

        @Override
        public boolean isLeaf() {
            return false;
        }

        @Override
        public PetrinetNode getParent() {
            return (PetrinetNode)super.getParent();
        }

        public List<IRule> getRules() {
            final List<IRule> rules = new ArrayList<IRule>(children.size());

            for (Object c : children) {
                rules.add(((RuleNode)c).getRule());
            }

            return rules;
        }
    }

    static class PetrinetNode extends DefaultMutableTreeNode {

        private final DefaultMutableTreeNode rulesNode;

        PetrinetNode(String name, Engine engine) {
            super(new Named<Engine>(name, engine), true);
            rulesNode = new RulesNode();
            add(rulesNode);
        }

        public void addRule(String name, IRule rule) {
            rulesNode.add(new RuleNode(name, rule));
        }

        @Override
        public boolean isLeaf() {
            return false;
        }

        public Engine getEngine() {
            return ((Engine)getUserObject());
        }

        public IPetrinet getPetrinet() {
            return getEngine().getNet();
        }
    }

    static class RuleNode extends DefaultMutableTreeNode {

        private final String name;

        private final IRule rule;

        RuleNode(String name, IRule rule) {
            super(new Named<IRule>(name,rule), true);
            this.name = name;
            this.rule = rule;
        }

        @Override
        public RulesNode getParent() {
            return (RulesNode) super.getParent();
        }

        public String getName() {
            return name;
        }

        public IRule getRule() {
            return rule;
        }

        @Override
        public boolean isLeaf() {
            return true;
        }
    }

    public PetrinetTreeModel(RootNode root) {
        super(root, false);
    }

    public PetrinetTreeModel() {
        this(new RootNode());
    }

    public PetrinetNode addPetrinet(String name, Engine engine) {
        PetrinetNode node = getRoot().addPetrinet(name, engine);
        nodeChanged(root);
        return node;
    }

    @Override
    public RootNode getRoot() {
        return (RootNode)root;
    }
}