package gui.fileTree;

import static gui.Style.*;
import gui.PetrinetPane;
import gui.RulePane;

import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

/**
 * custom mouse listener for use in petrinet tree extending {@link MouseListener}.
 */
public class TreeMouseListener implements MouseListener {

    /**
     * reference to the {@link JTree} object.
     */
    private JTree tree;

    /**
     * reference to the {@link PopupMenuListener}.
     */
    private ActionListener menuListener;

    /**
     * Constructor
     * @param tree reference to the {@link JTree} object.
     */
    public TreeMouseListener(JTree tree) {
        super();
        this.tree = tree;
        this.menuListener = PopupMenuListener.getInstance();
    }

    /**
     * dispatches the node type to show different popup menus.
     * @param event the mouse event triggered action.
     * @param node the node clicked.
     */
    private void showPopupMenu(MouseEvent event, Object node) {
        PetriTreeNode selectedNode = (PetriTreeNode) node;

        if (selectedNode.isNodeType(NodeType.NET_ROOT)) {
            this.showNetRootMenu(event, selectedNode);
        } else if (selectedNode.isNodeType(NodeType.RULE_ROOT)) {
            this.showRuleRootMenu(event, selectedNode);
        } else if (selectedNode.isNodeType(NodeType.NET)) {
            this.showNetMenu(event, selectedNode);
        } else if (selectedNode.isNodeType(NodeType.RULE)) {
            this.showRuleMenu(event, selectedNode);
        }else if (selectedNode.isNodeType(NodeType.NAC)) {
            this.showNacMenu(event, selectedNode);
        } else {
            // TODO: throw exception here
            System.out.println("unknown node type");
        }

    }

    /**
     * displays the popup menu for rules.
     * @param event the mouse event triggered action.
     * @param selectedNode the node clicked.
     */
    private void showRuleMenu(MouseEvent event, PetriTreeNode selectedNode) {
        JPopupMenu popup = new JPopupMenu();
        JMenuItem i;

        i = new JMenuItem(TREE_MENU_SAVE_ALL);
        popup.add(i);
        i.addActionListener(this.menuListener);

        popup.addSeparator();

        if (selectedNode.isChecked()) {
            i = new JMenuItem(TREE_MENU_UNCHECK_RULE);
        } else {
            i = new JMenuItem(TREE_MENU_CHECK_RULE);
        }
        popup.add(i);
        i.addActionListener(this.menuListener);

        i = new JMenuItem(TREE_MENU_SAVE);
        popup.add(i);
        i.addActionListener(this.menuListener);

        i = new JMenuItem(TREE_MENU_ADD_NAC);
        popup.add(i);
        i.addActionListener(this.menuListener);

        i = new JMenuItem(TREE_MENU_REMOVE_RULE);
        popup.add(i);
        i.addActionListener(this.menuListener);

        i = new JMenuItem(TREE_MENU_RELOAD_RULE);
        popup.add(i);
        i.addActionListener(this.menuListener);

        popup.show(tree, event.getX(), event.getY());
    }
    
    /**
     * displays the popup menu for nacs.
     * @param event the mouse event triggered action.
     * @param selectedNode the node clicked.
     */
    private void showNacMenu(MouseEvent event, PetriTreeNode selectedNode) {
        JPopupMenu popup = new JPopupMenu();
        JMenuItem i;

        i = new JMenuItem(TREE_MENU_SAVE_ALL);
        popup.add(i);
        i.addActionListener(this.menuListener);

        popup.addSeparator();

        i = new JMenuItem(TREE_MENU_REMOVE_NAC);
        popup.add(i);
        i.addActionListener(this.menuListener);

        popup.show(tree, event.getX(), event.getY());
    }

    /**
     * displays the popup menu for nets.
     * @param event the mouse event triggered action.
     * @param selectedNode the node clicked.
     */
    private void showNetMenu(MouseEvent e, DefaultMutableTreeNode selectedNode) {
        JPopupMenu popup = new JPopupMenu();
        JMenuItem i;

        i = new JMenuItem(TREE_MENU_SAVE_ALL);
        popup.add(i);
        i.addActionListener(this.menuListener);

        popup.addSeparator();

        i = new JMenuItem(TREE_MENU_SAVE);
        popup.add(i);
        i.addActionListener(this.menuListener);

        i = new JMenuItem(TREE_MENU_REMOVE_NET);
        popup.add(i);
        i.addActionListener(this.menuListener);

        i = new JMenuItem(TREE_MENU_RELOAD_NET);
        popup.add(i);
        i.addActionListener(this.menuListener);

        popup.show(tree, e.getX(), e.getY());
    }

    /**
     * displays the popup menu for rule root.
     * @param event the mouse event triggered action.
     * @param selectedNode the node clicked.
     */
    private void showRuleRootMenu(MouseEvent e, DefaultMutableTreeNode selectedNode) {
        JPopupMenu popup = new JPopupMenu();
        JMenuItem i;

        i = new JMenuItem(TREE_MENU_SAVE_ALL);
        popup.add(i);
        i.addActionListener(this.menuListener);

        popup.addSeparator();

        i = new JMenuItem(TREE_MENU_ADD_RULE);
        popup.add(i);
        i.addActionListener(this.menuListener);

        i = new JMenuItem(TREE_MENU_LOAD_RULE);
        popup.add(i);
        i.addActionListener(this.menuListener);

        popup.show(tree, e.getX(), e.getY());
    }

    /**
     * displays the popup menu for net root.
     * @param event the mouse event triggered action.
     * @param selectedNode the node clicked.
     */
    private void showNetRootMenu(MouseEvent e, DefaultMutableTreeNode selectedNode) {

        JPopupMenu popup = new JPopupMenu();
        JMenuItem i;

        i = new JMenuItem(TREE_MENU_SAVE_ALL);
        popup.add(i);
        i.addActionListener(this.menuListener);

        popup.addSeparator();

        i = new JMenuItem(TREE_MENU_ADD_NET);
        popup.add(i);
        i.addActionListener(this.menuListener);

        i = new JMenuItem(TREE_MENU_LOAD_NET);
        popup.add(i);
        i.addActionListener(this.menuListener);

        popup.show(tree, e.getX(), e.getY());
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        TreePath path = this.tree.getPathForLocation(e.getX(), e.getY());

        if (path != null) {
            this.tree.setSelectionPath(path);
            PetriTreeNode node = (PetriTreeNode) path.getLastPathComponent();

            String name = node.toString();
            Integer id = PopupMenuListener.getInstance().getPidOf(name);

            if (node.isNodeType(NodeType.NET)) {
                PetrinetPane.getInstance().displayPetrinet(id, name);
                PetriTreeNode netRoot = (PetriTreeNode) node.getParent();
                PetriTreeNode child;
                int numberOfChilcds = netRoot.getChildCount();
                for (int i = 0; i < numberOfChilcds; i++) {
                    child = (PetriTreeNode) netRoot.getChildAt(i);
                    child.setSelected(false);
                }
                node.setSelected(true);
            } else if (node.isNodeType(NodeType.RULE)) {
                RulePane.getInstance().displayRule(id);
                PetriTreeNode ruleRoot = (PetriTreeNode) node.getParent();
                PetriTreeNode child;
                int numberOfChilcds = ruleRoot.getChildCount();
                for (int i = 0; i < numberOfChilcds; i++) {
                    child = (PetriTreeNode) ruleRoot.getChildAt(i);
                    child.setSelected(false);
                }
                node.setSelected(true);
            }

            if (e.getButton() == MouseEvent.BUTTON3) {
                showPopupMenu(e, path.getLastPathComponent());
            }
        }

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // not used
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // not used
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // not used
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // not used
    }

}