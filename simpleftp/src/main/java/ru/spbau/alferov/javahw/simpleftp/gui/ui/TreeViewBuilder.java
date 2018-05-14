package ru.spbau.alferov.javahw.simpleftp.gui.ui;

import javafx.scene.control.TreeItem;
import ru.spbau.alferov.javahw.simpleftp.gui.filetree.*;

import java.util.Stack;

/**
 * This is a {@link Visitor} implementation for building a tree view basing on a remote file tree.
 */
public class TreeViewBuilder implements Visitor {
    /**
     * This is the root of a tree.
     */
    private TreeItem<String> root;

    /**
     * This is a callstack.
     */
    private Stack<TreeItem<String>> curStack = new Stack<>();

    /**
     * This creates an item in last directory.
     */
    private void visitItem(String filename) {
        TreeItem<String> item = new TreeItem<>(filename);
        if (curStack.empty()) {
            root = item;
        } else {
            curStack.peek().getChildren().add(item);
        }
        curStack.add(item);
    }

    /**
     * This visits a directory.
     */
    @Override
    public void visitDirectory(RemoteDirectory directory) {
        visitItem(directory.getFileName());
    }

    /**
     * This visits a file.
     */
    @Override
    public void visitFile(RemoteFile file) {
        visitItem(file.getFileName());
    }

    /**
     * This visits an item and modifies the callstack.
     */
    @Override
    public void visit(RemoteFileNode node) throws VisitorException {
        node.accept(this);
        curStack.pop();
    }

    /**
     * This builds a TreeView.
     *
     * @return The root of the tree.
     */
    public static TreeItem<String> build(RemoteFileNode rootNode) {
        TreeViewBuilder builder = new TreeViewBuilder();
        try {
            builder.visit(rootNode);
        } catch (VisitorException e) {
            // This never happens actually.
        }
        return builder.root;
    }
}
